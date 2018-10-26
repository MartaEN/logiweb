package com.marta.logistika.controller;

import com.marta.logistika.dto.DriverRecord;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/drivers")
public class DriversController {

    private final DriverService driverService;
    private final CityService cityService;

    @Autowired
    public DriversController(DriverService driverService, CityService cityService) {
        this.driverService = driverService;
        this.cityService = cityService;
    }

    @GetMapping
    public String home(Model uiModel) {

        uiModel.addAttribute("drivers", driverService.listAll());

        return "office/drivers/list";
    }

    @GetMapping(value = "/add")
    public String addDriverForm(Model uiModel) {

        uiModel.addAttribute("driver", new DriverRecord());
        uiModel.addAttribute("cities", cityService.listAll());

        return "office/drivers/add";
    }

    @PostMapping(value = "/add")
    public String addDriver(
            @Valid @ModelAttribute("driver") DriverRecord driver,
            BindingResult bindingResult,
            Model uiModel) {

        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("cities", cityService.listAll());
            return "office/drivers/add";
        }

        driverService.add(driver);
        return "redirect:/drivers";
    }

    @GetMapping(value = "/{personalId}")
    public String editDriverForm(
            @PathVariable("personalId") String personalId,
            Model uiModel) {

        DriverRecord driver = driverService.findDriverByPersonalId(personalId);

        uiModel.addAttribute("driver", driver);
        uiModel.addAttribute("cities", cityService.listAll());

        return "office/drivers/edit";

    }

    @PostMapping(value = "/{personalId}")
    public String edit(
            @ModelAttribute("driver") DriverRecord driver,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/drivers/" + driver.getPersonalId();
        }

        driverService.update(driver);

        return "redirect:/drivers";
    }

    @GetMapping(value = "/remove")
    public String remove(@RequestParam String personalId) {

        driverService.remove(personalId);

        return "redirect:/drivers";

    }

    @GetMapping(value = "/find-drivers")
    @ResponseBody
    public List<DriverRecord> findDrivers(@RequestParam long ticketId) {

        return driverService.findDrivers(ticketId);
    }

}
