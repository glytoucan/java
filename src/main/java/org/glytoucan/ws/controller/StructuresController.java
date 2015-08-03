package org.glytoucan.ws.controller;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Structures")
public class StructuresController {

	@RequestMapping("/")
	public String root(Model model) {
		return "structures/index";
	}

	@RequestMapping("/index")
	public String index(Model model) {
		return "structures/index";
	}

	@RequestMapping("/graphical")
	public String graphical(Model model, HttpSession httpSession) {
		return "structures/graphical";
	}
	
	@RequestMapping("/structureSearch")
	public String structureSearch(Model model, HttpSession httpSession) {
		return "structures/structure_search";
	}
}