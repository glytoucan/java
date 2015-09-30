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
    public String findPetTop(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);;
		return "D3/D3";
    }
	
	@RequestMapping(value = "/D3_Tree/{ID}")
    public String findPet(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3/D3_Tree";
    }

	@RequestMapping(value = "/D3_motif_isomer/{ID}")
	public String findPet2(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3/D3_motif_isomer";
    }
	
	@RequestMapping(value = "/D3_structure/{ID}")
	public String findPet4(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3/D3_structure";
    }
	
	@RequestMapping(value = "/D3_subsumed/{ID}")
	public String findPet5(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3/D3_subsumed";
    }
	
	@RequestMapping(value = "/D3_dndTree/{ID}")
	public String findPet_dnd(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3/D3_dndTree";
    }
}
