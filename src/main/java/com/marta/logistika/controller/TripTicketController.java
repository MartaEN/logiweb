package com.marta.logistika.controller;

import com.marta.logistika.dto.TripTicketCreateForm;
import com.marta.logistika.dto.TruckFilterForm;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.TripTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping(value = "/{id}/view")
    public String editCityLinks(@PathVariable("id") Long id, Model uiModel) {

        uiModel.addAttribute("ticket", ticketService.findDtoById(id));
        uiModel.addAttribute("orders", ticketService.listAllOrderInTicket(id));

        return "tickets/view";
    }

    @GetMapping(value = "/{id}/approve")
    public String approveTicket(@PathVariable("id") Long id, Model uiModel) {

        ticketService.approveTripTicket(id);

        return "redirect:/orders";
    }

    @GetMapping(value = "/create")
    public String newTicketForm(Model uiModel) {

        uiModel.addAttribute("filterForm", new TruckFilterForm());
        uiModel.addAttribute("cities", cityService.listAll());

        return "tickets/create";
    }

    @PostMapping(value = "/create")
    public String createNewTicket (@ModelAttribute TripTicketCreateForm ticketCreateForm) {

        ticketService.createTripTicket(
                ticketCreateForm.getTruckRegNumber(),
                LocalDateTime.parse(ticketCreateForm.getDepartureDate()),
                ticketCreateForm.getToCity() == null ? null : cityService.findById(ticketCreateForm.getToCity()));

        return "redirect:/orders";
    }

}
