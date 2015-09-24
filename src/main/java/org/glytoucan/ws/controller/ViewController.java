package org.glytoucan.ws.controller;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

	@Value("${application.message:Hello World}")
	private String message = "Hello World";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "welcome";
	}
	
	@RequestMapping(value = "/D3/{ID}")
    public String findPet(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3";
    }
	
	@RequestMapping(value = "/D3_motif_isomer/{ID}")
	public String findPet2(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3_motif_isomer";
    }
	
	@RequestMapping(value = "/D3_structure_subsume/{ID}")
	public String findPet4(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3_structure_subsume";
    }
	
	@RequestMapping(value = "/D3_dndTree/{ID}")
	public String findPet_dnd(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3_dndTree";
    }
}
