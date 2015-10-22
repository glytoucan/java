package org.glytoucan.ws.controller;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.service.UserProcedure;
import org.glytoucan.ws.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (userInfo == null) {
				redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
				return "redirect:/signout";
			}
			SparqlEntity userData = null;
			try {
				userData = userProcedure.getUser(userInfo.getEmail());
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

//	@RequestMapping("/index")
//	public String index(Model model) {
//		return "structures/index";
//	}
//
//	@RequestMapping("/graphical")
//	public String graphical(Model model, HttpSession httpSession) {
//		return "structures/graphical";
//	}
//	
//	@RequestMapping(value="/structureSearch", method = RequestMethod.GET)
//	public String structureSearch(Model model, @ModelAttribute("sequence") Sequence sequence, BindingResult result/*, @RequestParam(value="errorMessage", required=false) String errorMessage*/) {
////		logger.debug(errorMessage);
////		model.addAttribute("errorMessage", errorMessage);
//		return "structures/structure_search";
//	}
//
//	@RequestMapping("/structure")
//    public String structure(@ModelAttribute("sequence") Sequence sequence, BindingResult result, RedirectAttributes redirectAttrs) {
//		logger.debug(sequence);
//        if (StringUtils.isEmpty(sequence.getSequence())) {
//        	
//        	logger.debug("adding errorMessage:>input sequence<");
//        	redirectAttrs.addFlashAttribute("errorMessage", "Please input a sequence");
//        	return "redirect:/Structures/structureSearch";
//        } else {
//    		logger.debug("text1>" + sequence.getSequence() + "<");
//    		// check RDF for wurcs
//    		
//    		// if not wurcs, convert
//
//    		// get image
//    		// get accession number
//    		
//    		// embed wurcs, image, accNum into model
//    		
//            return "structures/structure";
//        }
//    }
//	
//	@RequestMapping("/test")
//    public String test(RedirectAttributes redirectAttrs) {
//		redirectAttrs.addFlashAttribute("test", "why not");
//		return "redirect:/Structures/testout";
//	}
//	
//	@RequestMapping(value="/testout", method = RequestMethod.GET)
//	public String testout() {
//		return "structures/test";
//	}

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
}