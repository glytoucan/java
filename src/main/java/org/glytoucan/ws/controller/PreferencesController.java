package org.glytoucan.ws.controller;
import javax.servlet.http.HttpSession;

import org.glytoucan.view.LocalizationHandlerMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.knappsack.swagger4springweb.annotation.ApiExclude;

@Controller
@RequestMapping("/Preferences")
@ApiExclude
public class PreferencesController {

	@RequestMapping("/")
	public String root(Model model) {
		return "preferences/index";
	}

	@RequestMapping("/index")
	public String index(Model model) {
		return "preferences/index";
	}
	
	@RequestMapping("/en")
	public String en(Model model, HttpSession httpSession) {
		httpSession.setAttribute(LocalizationHandlerMapping.LANGUAGE, "1");
		return "preferences/index";
	}
	
	@RequestMapping("/ja")
	public String ja(Model model, HttpSession httpSession) {
		httpSession.setAttribute(LocalizationHandlerMapping.LANGUAGE, "2");
		return "preferences/index";
	}
	
	@RequestMapping("/ch1")
	public String ch1(Model model, HttpSession httpSession) {
		httpSession.setAttribute(LocalizationHandlerMapping.LANGUAGE, "3");
		return "preferences/index";
	}
	
	@RequestMapping("/ch2")
	public String ch2(Model model, HttpSession httpSession) {
		httpSession.setAttribute(LocalizationHandlerMapping.LANGUAGE, "4");
		return "preferences/index";
	}
	
	@RequestMapping("/fr")
	public String fr(Model model, HttpSession httpSession) {
		httpSession.setAttribute(LocalizationHandlerMapping.LANGUAGE, "5");
		return "preferences/index";
	}
	
	@RequestMapping("/de")
	public String de(Model model, HttpSession httpSession) {
		httpSession.setAttribute(LocalizationHandlerMapping.LANGUAGE, "6");
		return "preferences/index";
	}
	
	@RequestMapping("/ru")
	public String ru(Model model, HttpSession httpSession) {
		httpSession.setAttribute(LocalizationHandlerMapping.LANGUAGE, "7");
		return "preferences/index";
	}
	
	@RequestMapping("/image/{imageNotation}")
	public String image(@PathVariable String imageNotation, Model model, HttpSession httpSession) {
		httpSession.setAttribute(LocalizationHandlerMapping.IMAGENOTATION, imageNotation);
		return "preferences/index";
	}
}