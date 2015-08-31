package org.glytoucan.ws.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.knappsack.swagger4springweb.annotation.ApiExclude;

@Controller
@ApiExclude
public class ViewController {
	@RequestMapping(value = "/D3/{ID}")
    public String findPet(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3";
    }
}