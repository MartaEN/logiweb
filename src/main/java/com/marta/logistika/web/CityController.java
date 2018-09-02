package com.marta.logistika.web;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.api.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @RequestMapping(value="/list", method=RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView model = new ModelAndView("city/list");
        List list = cityService.listAll();
        model.addObject("list", list);

        return model;
    }

}
