package org.glytoucan.web.controller;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.knappsack.swagger4springweb.annotation.ApiExclude;

@Controller
@RequestMapping("/Motifs")
@ApiExclude
public class MotifsController {

	@RequestMapping("/")
	public String root(Model model) {
		return "motifs/search";
	}

	@RequestMapping("/index")
	public String index(Model model) {
		return "motifs/search";
	}

	@RequestMapping("/search")
	public String search(Model model, HttpSession httpSession) {
		return "motifs/search";
	}
	
	@RequestMapping("/listAll")
	public String list(Model model) {
		return "motifs/list";
	}
}