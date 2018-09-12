package com.marta.logistika.controller;

import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;

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
    public String home(Model uiModel) {

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

        if( isTruckRecordValid(truck)) {
            truck.setParked(true);
            truckService.add(truck);
            return ("redirect:/trucks");
        } else {
            return "error";
        }

    }

    @GetMapping(value = "/{regNumber}")
    public String editTruckForm(
            @PathVariable("regNumber") String regNumber,
            Model uiModel) {

        TruckRecord truck = truckService.getTruckByRegNum(regNumber);

        uiModel.addAttribute("truck", truck);

        return "trucks/edit";

    }

    @PostMapping(value = "/{regNumber}")
    public String edit(
            @ModelAttribute("truck") TruckRecord truck) {

        if(isTruckRecordValid(truck)) {

            truckService.update(truck);
            return "redirect:/trucks";

        } else {

            return "error";
        }
    }

    @GetMapping(value = "/remove")
    public String remove(@RequestParam String regNumber) {

        truckService.remove(truckService.getTruckByRegNum(regNumber));

        return "redirect:/trucks";

    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {

        binder.registerCustomEditor(CityEntity.class, "location", new PropertyEditorSupport() {

            public void setAsText(String text) {
                Long id = Long.parseLong(text);
                CityEntity toCity = cityService.getCityById(id);
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

    private boolean isTruckRecordValid (TruckRecord truckRecord) {
        if ( ! truckRecord.getRegNumber().matches("^[a-zA-Z]{2}[0-9]{5}$")) return false;
        if ( truckRecord.getCapacity() < 0 ) return false;
        if ( truckRecord.getShiftSize() <0 || truckRecord.getShiftSize() > 2 ) return false;
        return true;
    }
}
