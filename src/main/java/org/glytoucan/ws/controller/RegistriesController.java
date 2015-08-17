package org.glytoucan.ws.controller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.service.UserProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Registries")
public class RegistriesController {
	Log logger = LogFactory.getLog(RegistriesController.class);

	// TODO: GlycanProcedure
	@Autowired
	UserProcedure userProcedure;
	
	@RequestMapping("/graphical")
	public String graphical(Model model, RedirectAttributes redirectAttrs) {
    	return "register/graphical";
	}

	@RequestMapping
	public String root(Model model) {
		return "register/index";
	}

	@RequestMapping("/upload")
	public String upload(Model model) {
		return "register/index";
	}
}