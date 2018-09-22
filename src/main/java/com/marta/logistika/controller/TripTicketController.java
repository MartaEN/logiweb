package com.marta.logistika.controller;

import com.marta.logistika.dto.DriverIdRecord;
import com.marta.logistika.dto.TripTicketCreateForm;
import com.marta.logistika.dto.TruckFilterForm;
import com.marta.logistika.service.api.CityService;
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

    @Autowired
    public TripTicketController(TripTicketService ticketService, CityService cityService) {
        this.ticketService = ticketService;
        this.cityService = cityService;
    }

    @GetMapping(value = "/{id}")
    public String viewTicket(@PathVariable("id") Long id, Model uiModel) {

        uiModel.addAttribute("ticket", ticketService.findDtoById(id));
        uiModel.addAttribute("orders", ticketService.listAllOrderInTicket(id));

        return "tickets/view";
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

    @PostMapping(value = "/{ticketId}/approve")
    public String approveTicket (
            @PathVariable("ticketId") long ticketId,
            @ModelAttribute ArrayList<DriverIdRecord> drivers) {

        //todo принимает список нулевой длины - как правильно принять данные?
        System.out.println(ticketId);
        return "redirect:/orders";
    }

}
