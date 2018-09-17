package com.marta.logistika.controller;

import com.marta.logistika.dto.DriverRecord;
import com.marta.logistika.service.ServiceException;
import com.marta.logistika.service.api.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public String home(Model uiModel) {

        uiModel.addAttribute("drivers", driverService.listAll());

        return "drivers/list";
    }

    @GetMapping(value = "/add")
    public String addDriverForm(Model uiModel) {
        uiModel.addAttribute("driver", new DriverRecord());
        return "drivers/add";
    }

    @PostMapping(value = "/add")
    public String addDriver(
            @ModelAttribute("driver") DriverRecord driver,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/drivers/add";
        }

        try {
            driverService.add(driver);
            return ("redirect:/drivers");
        } catch (ServiceException e) {
            e.printStackTrace();
            //todo develop error reporting to user
            return "error";
        }
    }

    @GetMapping(value = "/{personalId}")
    public String editDriverForm(
            @PathVariable("personalId") String personalId,
            Model uiModel) {

        DriverRecord driver = driverService.findDriverByPersonalId(personalId);

        uiModel.addAttribute("driver", driver);

        return "drivers/edit";

    }

    @PostMapping(value = "/{personalId}")
    public String edit(
            @ModelAttribute("driver") DriverRecord driver) {

        System.out.println(driver);
        try {
            driverService.update(driver);
            return "redirect:/drivers";
        } catch (ServiceException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping(value = "/remove")
    public String remove(@RequestParam String personalId) {

        driverService.remove(driverService.findDriverByPersonalId(personalId));

        return "redirect:/drivers";

    }
}
