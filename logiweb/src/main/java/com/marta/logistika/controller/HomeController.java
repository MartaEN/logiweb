package com.marta.logistika.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        return "redirect:/orders";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "common/login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "common/403";
    }

}
