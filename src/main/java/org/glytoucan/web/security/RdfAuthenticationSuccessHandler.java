package org.glytoucan.web.security;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.admin.client.UserClient;
import org.glytoucan.admin.model.ErrorCode;
import org.glytoucan.admin.model.User;
import org.glytoucan.admin.model.UserGenerateKeyRequest;
import org.glytoucan.admin.model.UserGenerateKeyResponse;
import org.glytoucan.admin.model.UserKeyRequest;
import org.glytoucan.admin.model.UserKeyResponse;
import org.glytoucan.admin.model.UserRegisterRequest;
import org.glytoucan.client.ContributorRest;
import org.glytoucan.client.model.RegisterContributorResponse;
import org.glytoucan.model.Message;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.github.fromi.openidconnect.security.UserInfo;

@Component
public class RdfAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {
	public static Log logger = (Log) LogFactory
			.getLog(RdfAuthenticationSuccessHandler.class);

	@Autowired
	ContributorRest contributorRest;

	@Autowired
	UserClient userClient;
	
	@Autowired(required=false)
	JavaMailSender mailSender;

	String[] requiredFields = {"email", "givenName", "familyName", "verifiedEmail"};

	/**
	 * 
	 * Redesigned.  Focusing on future user registrations, security, privacy, and the ability to host the web front end separate from the web services.
	 * 
	 * The procedural flow is explained in the Admin API documentation.
	 * 
	 * @see org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		
		// newly registered user
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        OAuth2AccessToken token = (OAuth2AccessToken)authentication.getCredentials();

        
        logger.debug("token:>" + token.getValue());
        logger.debug("adminKey:>" + adminKey);
        
		logger.debug("userinfo:>" + userInfo);
		Map<String, String> objectAsMap;
	    try {
			objectAsMap = BeanUtils.describe(userInfo);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new ServletException(e);
		}
		objectAsMap.remove("picture");
		objectAsMap.remove("link");
		
    if (!objectAsMap.keySet().containsAll(Arrays.asList(requiredFields))) {
      logger.error("fail");
    }
    
    org.glytoucan.admin.model.Authentication auth = new org.glytoucan.admin.model.Authentication();
    auth.setId(adminEmail);
    auth.setApiKey(adminKey);

    if (adminEmail.equals(userInfo.getEmail()) && StringUtils.isBlank(adminKey)) {
      logger.debug("admin key is blank");
      if (null == token || StringUtils.isBlank(token.getValue()))
        throw new ServletException("invalid authentication: blank token");

      UserKeyRequest ukr = new UserKeyRequest();
      auth.setApiKey(token.getValue());
      ukr.setAuthentication(auth);
      ukr.setPrimaryId(adminEmail);

      UserKeyResponse userKey = userClient.getKey(ukr);
      
      logger.debug("error code:>" + userKey.getResponseMessage().getErrorCode());
      logger.debug("user key response:>" + userKey.getKey());
      if (userKey.getResponseMessage().getErrorCode().equals(ErrorCode.AUTHENTICATION_FAILURE.toString()))
        throw new ServletException("authentication failure " + userKey.getResponseMessage().getErrorCode());        
      
      if (StringUtils.isEmpty(userKey.getKey())) {
        // generate User
        UserRegisterRequest urr = new UserRegisterRequest();
        urr.setAuthentication(auth);
        User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setGivenName(userInfo.getGivenName());
        user.setFamilyName(userInfo.getFamilyName());
        user.setEmailVerified(userInfo.getVerifiedEmail());
        user.setExternalId("815e7cbca52763e5c3fbb5a4dccc176479a50e2367f920843c4c35dca112e33d");
        urr.setUser(user);
        
        userClient.register(urr);
        
        UserGenerateKeyRequest req = new UserGenerateKeyRequest();
        req.setAuthentication(auth);
        req.setPrimaryId(adminEmail);
        UserGenerateKeyResponse ugkr = userClient.generateKey(req);
        if (ugkr.getResponseMessage().getErrorCode().equals("0"))
          adminKey = ugkr.getKey();
      } else
        adminKey=userKey.getKey();
      logger.debug("admin key is:>" + adminKey);
      auth.setApiKey(adminKey);
    } else {
      
      if (StringUtils.isBlank(adminKey))
        throw new ServletException("please wait until admin has logged in");
// add Contributor limited to admin 
      Map<String, Object>  map = new HashMap<String, Object>();
      map.put(ContributorRest.NAME, userInfo.getGivenName());
      map.put(ContributorRest.EMAIL, userInfo.getEmail());
      map.put(ContributorRest.USERNAME, adminEmail);
      map.put(ContributorRest.API_KEY, adminKey);
      
      Map<String, Object>  results = contributorRest.register(map);
      
      RegisterContributorResponse result = (RegisterContributorResponse) results.get(ContributorRest.MESSAGE);
      logger.debug("contributor" + result.getContributorId() +  " successfully created");

  
      UserRegisterRequest urr = new UserRegisterRequest();
      urr.setAuthentication(auth);
      User user = new User();
      user.setEmail(userInfo.getEmail());
      user.setGivenName(userInfo.getGivenName());
      user.setFamilyName(userInfo.getFamilyName());
      user.setEmailVerified(userInfo.getVerifiedEmail());
      user.setExternalId(result.getContributorId());
      urr.setUser(user);
      
      userClient.register(urr);
    
    }
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
  @Value("${admin.email:glytoucan@gmail.com}")
  private String adminEmail;
  
  @Value("${admin.key: }")
  private String adminKey;
}