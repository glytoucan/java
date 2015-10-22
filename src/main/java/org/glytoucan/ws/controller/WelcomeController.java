package org.glytoucan.ws.controller;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.knappsack.swagger4springweb.annotation.ApiExclude;
@Controller
@ApiExclude
public class WelcomeController {
	Log logger = LogFactory.getLog(WelcomeController.class);

	@RequestMapping("/")
	public String welcome(Model model) {
		return "index";
	}

	@RequestMapping("/init")
	public String init(Model model) {
		return "init";
	}

	@RequestMapping("/signout")
	public String signout(Model model, HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("/signin")
	public String signin(Model model, HttpSession session) {
		session.invalidate();
		logger.debug("signin");
		return "redirect:/?errorMessage=something happened with signin";
	}
}