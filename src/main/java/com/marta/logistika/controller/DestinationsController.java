package com.marta.logistika.controller;

import com.marta.logistika.dto.RoadRecord;
import com.marta.logistika.dto.RouteJsonResponse;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.RoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
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
    public String home(Model uiModel) {

        uiModel.addAttribute("cities", cityService.listAll());

        return "destinations/list";
    }

    @GetMapping(value = "/{id}")
    public String editCityLinks(@PathVariable("id") Long id, Model uiModel) {

        CityEntity city = cityService.findById(id);
        List<RoadRecord> roads = roadService.listAllRoadsFrom(city);

        uiModel.addAttribute("city", city);
        uiModel.addAttribute("roads", roads);

        return "destinations/edit";

    }

    @GetMapping(value = "/add-city")
    public String addCityForm(Model uiModel) {
        CityEntity city = new CityEntity();
        uiModel.addAttribute("city", city);
        return "destinations/add-city";
    }

    @PostMapping(value = "/add-city")
    public String addCity(
            @ModelAttribute("city") CityEntity city,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/destinations/add-city";
        }
        cityService.add(city);
        long id = city.getId();
        return ("redirect:/destinations/" + id);
    }

    @GetMapping(value = "/{id}/remove")
    public String removeCity(@PathVariable("id") Long id) {

        cityService.remove(id);

        return "redirect:/destinations";

    }

    @GetMapping(value = "/{id}/add-road")
    public String addRoadForm(@PathVariable("id") Long id, Model uiModel) {
        CityEntity fromCity = cityService.findById(id);
        RoadRecord road = new RoadRecord();

        uiModel.addAttribute("fromCity", fromCity);
        uiModel.addAttribute("road", road);
        uiModel.addAttribute("cities", cityService.listAll());

        return "destinations/add-road";
    }

    @PostMapping(value = "/{id}/add-road")
    public String addRoad(
            @PathVariable("id") Long id,
            @ModelAttribute("road") RoadRecord roadRecord,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/destinations/{id}/add-road";
        }

        RoadEntity road = new RoadEntity();
        road.setFromCity(cityService.findById(id));
        road.setToCity(roadRecord.getToCity());
        road.setDistance(roadRecord.getDistance());
        roadService.add(road);

        return "redirect:/destinations/{id}";
    }

    @GetMapping(value = "/{cityId}/remove-road/{roadId}")
    public String removeRoad(
            @PathVariable("roadId") Long roadId) {

        roadService.remove(roadId);

        return "redirect:/destinations/{cityId}";
    }

    @GetMapping(value = "/find-route")
    public String findRouteForm(Model uiModel) {

        uiModel.addAttribute("road", new RoadRecord());
        uiModel.addAttribute("cities", cityService.listAll());

        return "destinations/find-route";
    }

    @GetMapping(value = "/find-route-result", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public RouteJsonResponse findRouteResult(
            @RequestParam long fromCityId,
            @RequestParam long toCityId) {

        RouteJsonResponse response = new RouteJsonResponse();
        response.setRoute(roadService.findRouteFromTo(cityService.findById(fromCityId), cityService.findById(toCityId)));
        return response;

    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {

        binder.registerCustomEditor(CityEntity.class, "fromCity", new PropertyEditorSupport() {

            public void setAsText(String text) {
                Long id = Long.parseLong(text);
                CityEntity toCity = cityService.findById(id);
                setValue(toCity);
            }

            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    CityEntity city = (CityEntity) value;
                    return city.getName();
                }
                return null;
            }
        });

        binder.registerCustomEditor(CityEntity.class, "toCity", new PropertyEditorSupport() {

            public void setAsText(String text) {
                Long id = Long.parseLong(text);
                CityEntity toCity = cityService.findById(id);
                setValue(toCity);
            }

            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    CityEntity city = (CityEntity) value;
                    return city.getName();
                }
                return null;
            }
        });

    }

}
