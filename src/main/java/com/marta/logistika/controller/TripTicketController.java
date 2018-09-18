package com.marta.logistika.controller;

import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.service.api.TripTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TripTicketController {

    private final TripTicketService ticketService;

    @Autowired
    public TripTicketController(TripTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping(value = "/{id}/view")
    public String editCityLinks(@PathVariable("id") Long id, Model uiModel) {

        TripTicketRecord ticket = ticketService.findDtoById(id);
        List<OrderEntity> orders = ticketService.listAllOrderInTicket(id);

        uiModel.addAttribute("ticket", ticket);
        uiModel.addAttribute("orders", orders);

        return "tickets/view";
    }

    @GetMapping(value = "/{id}/approve")
    public String approveTicket(@PathVariable("id") Long id, Model uiModel) {
        ticketService.approveTripTicket(id);
        return "orders";
    }
}
