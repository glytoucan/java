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
	@RequestMapping(value = "/D3/{ID}")
    public String findPet(@PathVariable String ID, Model model) {
		model.addAttribute("ID", ID);
		return "D3";
    }
}