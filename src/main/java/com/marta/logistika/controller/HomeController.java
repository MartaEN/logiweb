package com.marta.logistika.web;

import com.marta.logistika.service.api.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private CityService cityService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Model uiModel){

		return "home/main";
	}	
		
}
