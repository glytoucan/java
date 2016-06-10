package org.glytoucan.ws.controller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.service.UserProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
	UserProcedure userProcedure;
	
	@RequestMapping("/profile")
	public String profile(Model model, RedirectAttributes redirectAttrs) {
//    	redirectAttrs.addFlashAttribute("infoMessage", "Logged In");
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserInfo) {
			UserInfo userInfo = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (userInfo == null) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			}
			SparqlEntity userData = null;
			try {
				userData = userProcedure.getById(userInfo.getId());
			} catch (SparqlException e) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			};
			if (null == userData) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			}
			
			logger.debug(userData);
			model.addAttribute("userProfile", userData.getData());
			
			// TODO: check for verified email in RDF
	        if (userInfo.getVerifiedEmail()!=null && userInfo.getVerifiedEmail().equals("true")) {
	        	model.addAttribute("verifiedEmail", true);
	        }
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
			try {
				hash = userProcedure.generateHash(userInfo.getId());
			} catch (SparqlException e) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			};
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
	      try {
	        userData = userProcedure.getById(userInfo.getId());
	      } catch (SparqlException e) {
	        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
	        return "redirect:/signout";
	      };
	      if (null == userData) {
	        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
	        return "redirect:/signout";
	      }
	      
        logger.debug("userId:>" + userData.getValue(UserProcedure.CONTRIBUTOR_ID) + "<");
	      logger.debug(userData);
	      model.addAttribute("contributorUserId", userData.getValue(UserProcedure.CONTRIBUTOR_ID));
	    } else {
        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
	      return "redirect:/";
	    }
	    
	      return "users/my_structure";
	  }
}