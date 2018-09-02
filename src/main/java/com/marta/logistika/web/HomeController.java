package com.marta.logistika.web;


import com.marta.logistika.dto.CityRecord;
import com.marta.logistika.service.api.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private CityService cityService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Model uiModel){

		List<CityRecord> cities = cityService.listAll();
		uiModel.addAttribute("cities", cities);
		
		return "home/main";
	}	
		
}
