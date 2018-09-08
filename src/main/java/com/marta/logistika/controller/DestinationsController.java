package com.marta.logistika.controller;

import com.marta.logistika.dto.RoadRecord;
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

    @GetMapping
    public String home(Model uiModel){

        uiModel.addAttribute("cities", cityService.listAll());

        return "destinations/monitor";
    }

    @GetMapping(value="/{id}")
    public String editCityLinks(@PathVariable("id") Long id, Model uiModel){

        CityEntity city = cityService.getCityById(id);
        List<RoadEntity> roads = roadService.listAllRoadsFrom(city);

        uiModel.addAttribute("city", city);
        uiModel.addAttribute("roads", roads);

        return "destinations/edit";

    }

    @GetMapping(value="/add-city")
    public String addCityForm(Model uiModel){
        CityEntity city = new CityEntity();
        uiModel.addAttribute("city", city);
        return "destinations/add-city";
    }

    @PostMapping(value="/add-city")
    public String addCity(
            @ModelAttribute("city") CityEntity city,
            BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "redirect:/destinations/add-city";
        }
        cityService.add(city);
        return "redirect:/destinations";
    }

    @GetMapping(value="/{id}/remove")
    public String removeCity(@PathVariable("id") Long id){

        cityService.removeCity(id);

        return "redirect:/destinations";

    }

    @GetMapping(value="/{id}/add-road")
    public String addRoadForm(@PathVariable("id") Long id, Model uiModel){
        CityEntity startCity = cityService.getCityById(id);
        RoadRecord road = new RoadRecord();

        uiModel.addAttribute("startCity", startCity);
        uiModel.addAttribute("road", road);
        uiModel.addAttribute("cities", cityService.listAll());

        return "destinations/add-road";
    }

    @PostMapping(value="/{id}/add-road")
    public String addRoad(
            @PathVariable("id") Long id,
            @ModelAttribute("road") RoadRecord road,
            BindingResult bindingResult){

        System.out.println(road);

        if(bindingResult.hasErrors()){
            return "redirect:/destinations/{id}/add-road";
        }

        RoadEntity newRoad = new RoadEntity();
        newRoad.setFromCity(cityService.getCityById(id));
        newRoad.setToCity(cityService.getCityById(road.getToCityId()));
        newRoad.setDistance(road.getDistance());
        roadService.add(newRoad);

        return "redirect:/destinations/{id}";
    }

    @GetMapping(value="/{cityId}/remove-road/{roadId}")
    public String removeRoad(
            @PathVariable("cityId") Long cityId,
            @PathVariable("roadId") Long roadId){

        roadService.remove(roadId);

        return "redirect:/destinations/{cityId}";
    }

}
