package com.marta.logistika.controller;

import com.marta.logistika.dto.TruckFilterForm;
import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/trucks")
public class TruckController {

    private final TruckService truckService;
    private final CityService cityService;

    @Autowired
    public TruckController(TruckService truckService, CityService cityService) {
        this.truckService = truckService;
        this.cityService = cityService;
    }

    @GetMapping
    public String showTruckList(Model uiModel) {

        uiModel.addAttribute("trucks", truckService.listAll());

        return "office/trucks/list";
    }

    @GetMapping(value = "/add")
    public String addTruckForm(Model uiModel) {
        uiModel.addAttribute("truck", new TruckRecord());
        uiModel.addAttribute("cities", cityService.listAll());
        return "office/trucks/add";
    }

    @PostMapping(value = "/add")
    public String addTruck(
            @ModelAttribute("truck") TruckRecord truck,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/trucks/add";
        }

        truckService.add(truck);
        return ("redirect:/trucks");

    }

    @GetMapping(value = "/{regNumber}")
    public String editTruckForm(
            @PathVariable("regNumber") String regNumber,
            Model uiModel) {

        TruckRecord truck = truckService.findTruckByRegNum(regNumber);

        uiModel.addAttribute("truck", truck);

        return "office/trucks/edit";

    }

    @PostMapping(value = "/{regNumber}")
    public String edit(
            @ModelAttribute("truck") TruckRecord truck) {

        truckService.update(truck);

        return "redirect:/trucks";
    }

    @GetMapping(value = "/remove")
    public String remove(@RequestParam String regNumber) {

        truckService.remove(truckService.findTruckByRegNum(regNumber));

        return "redirect:/trucks";

    }

    @GetMapping(value = "/find-truck-list", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<TruckRecord> findTruckList(
            @RequestParam String departureDateTime,
            @RequestParam long fromCity,
            @RequestParam int minCapacity,
            @RequestParam int maxCapacity) {

        TruckFilterForm filterForm = new TruckFilterForm();
        filterForm.setDepartureDateTime(LocalDateTime.parse(departureDateTime));
        filterForm.setFromCity(cityService.findById(fromCity));
        filterForm.setMinCapacity(minCapacity);
        filterForm.setMaxCapacity(maxCapacity);

        return truckService.listAllFilteredBy(filterForm);
    }

}
