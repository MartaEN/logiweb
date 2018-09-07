package com.marta.logistika.web;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.RoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/destinations")
public class DestinationsController {


    private final CityService cityService;
    private final RoadService roadService;

    @Autowired
    public DestinationsController(CityService cityService, RoadService roadService) {
        this.cityService = cityService;
        this.roadService = roadService;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String home(Model uiModel){

        uiModel.addAttribute("cities", cityService.listAll());

        return "destinations/monitor";
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public String editCityLinks(@PathVariable("id") Long id, Model uiModel){

        CityEntity city = cityService.getCityById(id);
        List<RoadEntity> roads = roadService.listAllRoadsFrom(city);

        uiModel.addAttribute("city", city);
        uiModel.addAttribute("roads", roads);

        return "destinations/edit";

    }

    @RequestMapping(value="/add-city", method= RequestMethod.GET)
    public String addCityForm(Model uiModel){
        CityEntity city = new CityEntity();
        uiModel.addAttribute("city", city);
        return "destinations/add-city";
    }

    @RequestMapping(value="/add-city", method=RequestMethod.POST)
    public String addCity(
            @ModelAttribute("city") CityEntity city,
            BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "redirect:/destinations/add-city";
        }
        cityService.add(city);
        return "redirect:/destinations";
    }

    @RequestMapping(value="/{id}/add-road", method= RequestMethod.GET)
    public String addRoadForm(@PathVariable("id") Long id, Model uiModel){
        CityEntity startCity = cityService.getCityById(id);
        RoadEntity road = new RoadEntity();
        road.setFromCity(startCity);

        uiModel.addAttribute("startCity", startCity);
        uiModel.addAttribute("road", road);
        uiModel.addAttribute("cities", cityService.listAll());

        return "destinations/add-road";
    }

    @RequestMapping(value="/{id}/add-road", method=RequestMethod.POST)
    public String addRoad(
            @PathVariable("id") Long id,
            @ModelAttribute("road") RoadEntity road,
            BindingResult bindingResult){

        //TODO сделать, чтобы записывалось
        System.out.println(road);

        if(bindingResult.hasErrors()){
            return "redirect:/destinations/{id}/add-road";
        }

        RoadEntity newRoad = new RoadEntity();
        newRoad.setFromCity(cityService.getCityById(id));
        newRoad.setToCity(road.getToCity());
        newRoad.setDistance(road.getDistance());
        roadService.add(newRoad);

        return "redirect:/destinations/edit";
    }


}
