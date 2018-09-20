package com.marta.logistika.controller;

import com.marta.logistika.dto.OpenOrdersAndTicketsDTO;
import com.marta.logistika.dto.OrderEntryForm;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.OrderService;
import com.marta.logistika.service.api.TripTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;
    private final TripTicketService tripTicketService;
    private final CityService cityService;

    @Autowired
    public OrdersController(OrderService orderService, TripTicketService tripTicketService, CityService cityService) {
        this.orderService = orderService;
        this.tripTicketService = tripTicketService;
        this.cityService = cityService;
    }

    @GetMapping
    public String home(Model uiModel) {

        uiModel.addAttribute("orders", orderService.listAllUnassigned());
        uiModel.addAttribute("tickets", tripTicketService.listAllUnapproved());
        uiModel.addAttribute("cities", cityService.listAll());
        uiModel.addAttribute("orderEntryForm", new OrderEntryForm());

        return "orders/monitor";
    }

    @GetMapping(value = "/add-no-ajax")
    public String newOrderForm(Model uiModel) {

        uiModel.addAttribute("orderEntryForm",  new OrderEntryForm());
        uiModel.addAttribute("cities", cityService.listAll());
        return "orders/add-no-ajax";
    }

    @PostMapping(value = "/add-no-ajax")
    public String addNewOrder(
            @ModelAttribute("road") OrderEntryForm orderEntryForm,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/orders/add-no-ajax";
        }

        orderService.add(orderEntryForm);

        return "redirect:/orders";
    }


    @PostMapping("/add")
    public ResponseEntity newOrderFormProcessing(@RequestBody OrderEntryForm orderEntryForm) {
        orderService.add(orderEntryForm);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @GetMapping(value = "/add-order-to-ticket", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public OpenOrdersAndTicketsDTO addOrderToTicket(
            @RequestParam long orderId,
            @RequestParam long ticketId) {

        tripTicketService.addOrderToTicket(tripTicketService.findById(ticketId), orderService.findById(orderId));

        OpenOrdersAndTicketsDTO response = new OpenOrdersAndTicketsDTO();
        response.setOrders(orderService.listAllUnassigned());
        response.setTickets(tripTicketService.listAllUnapproved());

        return response;
    }

}
