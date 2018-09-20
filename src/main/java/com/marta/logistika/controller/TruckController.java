package com.marta.logistika.controller;

import com.marta.logistika.dto.TruckFilterForm;
import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.ServiceException;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
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

        return "trucks/list";
    }

    @GetMapping(value = "/add")
    public String addTruckForm(Model uiModel) {
        uiModel.addAttribute("truck", new TruckRecord());
        uiModel.addAttribute("cities", cityService.listAll());
        return "trucks/add";
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

        return "trucks/edit";

    }

    @PostMapping(value = "/{regNumber}")
    public String edit(
            @ModelAttribute("truck") TruckRecord truck) {

        try {
            truckService.update(truck);
            return "redirect:/trucks";
        } catch (ServiceException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping(value = "/remove")
    public String remove(@RequestParam String regNumber) {

        truckService.remove(truckService.findTruckByRegNum(regNumber));

        return "redirect:/trucks";

    }

    @GetMapping(value = "/find-truck-list", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<TruckRecord> findTruckList(
            @RequestParam String departureDate,
            @RequestParam long fromCity,
            @RequestParam int minCapacity,
            @RequestParam int maxCapacity) {

        TruckFilterForm filterForm = new TruckFilterForm();
        filterForm.setDepartureDate(LocalDateTime.parse(departureDate));
        filterForm.setFromCity(cityService.findById(fromCity));
        filterForm.setMinCapacity(minCapacity);
        filterForm.setMaxCapacity(maxCapacity);

        return truckService.listAllFilteredBy(filterForm);
    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {

        binder.registerCustomEditor(CityEntity.class, "location", new PropertyEditorSupport() {

            public void setAsText(String text) {
                Long id = Long.parseLong(text);
                CityEntity location = cityService.findById(id);
                setValue(location);
            }

            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    CityEntity location = (CityEntity) value;
                    return location.getName();
                }
                return null;
            }
        });
    }
}
