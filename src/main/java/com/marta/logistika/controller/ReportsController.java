package com.marta.logistika.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportsController {

    @GetMapping
    public String home(){
        return "redirect: /orders/list";
    }
}
