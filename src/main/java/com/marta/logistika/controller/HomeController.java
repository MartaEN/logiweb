package com.marta.logistika.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping
	public String home(){
		return "redirect:/orders";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
	    return "common/login";

	}

    @GetMapping("/access-denied")
    public String showAccessDenied() {
	    return "common/403";
    }
		
}
