package org.glytoucan.ws.controller;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.knappsack.swagger4springweb.annotation.ApiExclude;
@Controller
@ApiExclude
public class WelcomeController {
	
	@RequestMapping("/")
	public String welcome(Model model) {
		return "index";
	}
	
	@RequestMapping("/signout")
	public String signout(Model model, HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	
}