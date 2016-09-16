package org.glytoucan.web.controller;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.NumberGenerator;
import org.glytoucan.admin.client.UserClient;
//import org.glytoucan.admin.exception.UserException;
import org.glytoucan.admin.model.Authentication;
import org.glytoucan.admin.model.User;
import org.glytoucan.admin.model.UserDetailsRequest;
import org.glytoucan.admin.model.UserDetailsResponse;
import org.glytoucan.admin.model.UserGenerateKeyRequest;
import org.glytoucan.admin.model.UserGenerateKeyResponse;
import org.glytoucan.admin.model.UserKeyRequest;
import org.glytoucan.admin.model.UserKeyResponse;
//import org.glytoucan.admin.service.UserProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.fromi.openidconnect.security.UserInfo;
import com.knappsack.swagger4springweb.annotation.ApiExclude;

@Controller
@RequestMapping("/Users")
@ApiExclude
public class UserController {
	Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	UserClient userClient;
	
//	@Autowired
//	@Qualifier("adminKey")
//	String adminKey;
	
  @Value("${admin.key: }")
  private String adminKey;
  
	@RequestMapping("/profile")
	public String profile(Model model, RedirectAttributes redirectAttrs) {
//    	redirectAttrs.addFlashAttribute("infoMessage", "Logged In");
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserInfo) {
			UserInfo userInfo = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (userInfo == null) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			}
			
      OAuth2AccessToken token = (OAuth2AccessToken)SecurityContextHolder.getContext().getAuthentication().getCredentials();
      if (token == null || StringUtils.isBlank(token.getValue())) {
        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
        return "redirect:/signout";
      }
      logger.debug("token:>" + token.getValue());
      
//			SparqlEntity userData = null;
//			try {
			  UserDetailsRequest request = new UserDetailsRequest();
			  Authentication auth = new Authentication();
			  auth.setId(userInfo.getEmail());
//      logger.debug("adminKey:>" + adminKey);
        auth.setApiKey(token.getValue());
			  request.setAuthentication(auth);
			  request.setPrimaryId(userInfo.getEmail());
			  UserDetailsResponse response =  userClient.userDetailsRequest(request);
//				userData = userProcedure.getIdByEmail(userInfo.getEmail());
			  if (null == response || null == response.getUser()) {
			    redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
			    return "redirect:/signout";
			  }
			  User user = response.getUser();
//			};
			if (null == user.getGivenName()) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			}
			
			UserKeyRequest ukRequest = new UserKeyRequest();
			ukRequest.setAuthentication(auth);
			ukRequest.setPrimaryId(userInfo.getEmail());
			UserKeyResponse ukResponse = userClient.getKey(ukRequest);
			if (null != ukResponse && !StringUtils.isBlank(ukResponse.getKey())) {
			  user.setMembershipNumber(ukResponse.getKey());
			}
			
			logger.debug(user);
			model.addAttribute("userProfile", user);
			
			// TODO: check for verified email in RDF
	        if (userInfo.getVerifiedEmail()!=null && userInfo.getVerifiedEmail().equals("true")) {
	        	model.addAttribute("verifiedEmail", true);
	        } else
	          model.addAttribute("verifiedEmail", false);
		}
		
    	return "users/profile";
	}

	@RequestMapping("/generateKey")
	public String generateKey(Model model, RedirectAttributes redirectAttrs) {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (userInfo == null) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			}
			String hash = null;
			
//      SparqlEntity userData = null;
//      try {
      OAuth2AccessToken token = (OAuth2AccessToken)SecurityContextHolder.getContext().getAuthentication().getCredentials();
      if (token == null || StringUtils.isBlank(token.getValue())) {
        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
        return "redirect:/signout";
      }
      
      UserGenerateKeyRequest request = new UserGenerateKeyRequest();
      Authentication auth = new Authentication();
      auth.setId(userInfo.getEmail());
      auth.setApiKey(token.getValue());
      request.setAuthentication(auth);
      request.setPrimaryId(userInfo.getEmail());
      UserGenerateKeyResponse response =  userClient.generateKey(request);

        
      hash = response.getKey();
//			try {
//				hash = userProcedure.generateHash(userInfo.getEmail());
//			} catch (UserException e) {
//				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
//				return "redirect:/signout";
//			};
			if (null == hash) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			}
			
			logger.debug(hash);
		}
		
		return "redirect:/Users/profile";
	}
	
	 @RequestMapping("/structure")
	  public String structure(Model model, RedirectAttributes redirectAttrs) {
	    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserInfo) {
	      UserInfo userInfo = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	      if (userInfo == null) {
	        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
	        return "redirect:/signout";
	      }
	      SparqlEntity userData = null;
//	      try {
//	        userData = userProcedure.getById(userInfo.getEmail());
//	      } catch (UserException e) {
//	        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
//	        return "redirect:/signout";
//	      };
	      if (null == userData) {
	        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
	        return "redirect:/signout";
	      }
	      
//        logger.debug("userId:>" + userData.getValue(UserProcedure.CONTRIBUTOR_ID) + "<");
	      logger.debug(userData);
//	      model.addAttribute("contributorUserId", userData.getValue(UserProcedure.CONTRIBUTOR_ID));
	    } else {
        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
	      return "redirect:/";
	    }
	    
	      return "users/my_structure";
	  }
}