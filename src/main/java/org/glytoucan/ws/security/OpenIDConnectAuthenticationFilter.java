package org.glytoucan.ws.security;

import static java.util.Optional.empty;
import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.openid.AuthenticationCancelledException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class OpenIDConnectAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	static final Logger logger = LoggerFactory.getLogger(OpenIDConnectAuthenticationFilter.class);
	
    @Resource
    private OAuth2RestOperations restTemplate;

    protected OpenIDConnectAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authentication -> authentication); // AbstractAuthenticationProcessingFilter requires an authentication manager.
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
   	 	logger.debug("checking with google from:>" + request.getRequestURI() + "<");
   	 	try {
        final ResponseEntity<UserInfo> userInfoResponseEntity = restTemplate.getForEntity("https://www.googleapis.com/oauth2/v2/userinfo", UserInfo.class);
//   	 	ResponseEntity<UserInfo> userInfoResponseEntity ;
//   	 	try {
//        userInfoResponseEntity = restTemplate.getForEntity("https://www.googleapis.com/oauth2/v2/userinfo", UserInfo.class);
//   	 	} catch (Exception e) {
//   	 		e.printStackTrace();
//   	 		logger.debug(e.getMessage());
//   	 		throw e;
//   	  	}
   	 	logger.debug("UserInfo:>", userInfoResponseEntity);
        return new PreAuthenticatedAuthenticationToken(userInfoResponseEntity.getBody(), empty(), NO_AUTHORITIES);
   	 	} catch (UserDeniedAuthorizationException e) {
   	 		e.printStackTrace();
   	 		logger.debug("user denied:>" + e.getMessage());
   	 		throw new  AuthenticationCancelledException(e.getMessage(), e);
   	 	}
    }
}
