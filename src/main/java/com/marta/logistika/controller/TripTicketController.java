package com.marta.logistika.controller;

import com.marta.logistika.dto.*;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.DriverService;
import com.marta.logistika.service.api.TripTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/tickets")
public class TripTicketController {

    private final TripTicketService ticketService;
    private final CityService cityService;
    private final DriverService driverService;

    @Autowired
    public TripTicketController(TripTicketService ticketService, CityService cityService, DriverService driverService) {
        this.ticketService = ticketService;
        this.cityService = cityService;
        this.driverService = driverService;
    }

    @GetMapping(value = "/{id}")
    public String viewTicket(@PathVariable("id") Long id, Model uiModel) {

        uiModel.addAttribute("ticket", ticketService.findDtoById(id));
        uiModel.addAttribute("orders", ticketService.listAllOrderInTicket(id));

        return "tickets/approve";
    }

    @GetMapping(value = "/create")
    public String newTicketForm(Model uiModel) {

        uiModel.addAttribute("filterForm", new TruckFilterForm());
        uiModel.addAttribute("cities", cityService.listAll());

        return "tickets/create";
    }

    @PostMapping(value = "/create")
    public String newTicketCreate(@ModelAttribute TripTicketCreateForm ticketCreateForm) {

        ticketService.createTripTicket(
                ticketCreateForm.getTruckRegNumber(),
                LocalDateTime.parse(ticketCreateForm.getDepartureDateTime()),
                ticketCreateForm.getToCity() == null ? null : cityService.findById(ticketCreateForm.getToCity()));

        return "redirect:/orders";
    }

    @PostMapping(value = "/{ticketId}/sign")
    public String signTicket (@PathVariable("ticketId") long ticketId, Model uiModel) {

        ticketService.signTicket(ticketId);

        uiModel.addAttribute("ticket", ticketService.findDtoById(ticketId));
        uiModel.addAttribute("orders", ticketService.listAllOrderInTicket(ticketId));

        return "tickets/view";
    }


    //        <%--Неудавшийся multiple select через post spring формы--%>
    @GetMapping(value = "/{ticketId}/select-drivers")
    public String selectDrivers (
            @PathVariable("ticketId") long ticketId,
            Model uiModel) {

        uiModel.addAttribute("ticketId", ticketId);
        uiModel.addAttribute("shiftSize", ticketService.findDtoById(ticketId).getTruck().getShiftSize());
        uiModel.addAttribute("driverList", driverService.findDrivers(ticketId));
        uiModel.addAttribute("driverSelectForm", new DriverSelectForm());

        return "drivers/select";

    }

    //        <%--Неудавшийся multiple select через post spring формы--%>
    @PostMapping(value = "/{ticketId}/finalize")
    public String finalizeTicket (
            @ModelAttribute DriverSelectForm drivers,
            @PathVariable("ticketId") long ticketId) {

        System.out.println("=== I'm inside ====");
        return "redirect:/orders";
    }

    //            <%--Неудавшийся multiple select через ajax и обычную форму--%>
    @PostMapping(value = "/{ticketId}/approve", consumes = {"application/x-www-form-urlencoded"})
    public String approveTicket (
            @PathVariable("ticketId") long ticketId,
            ArrayList<DriverIdRecord> drivers) {

        //todo принимает список нулевой длины - как правильно принять данные?
        System.out.println(ticketId);
        return "redirect:/orders";
    }


}
