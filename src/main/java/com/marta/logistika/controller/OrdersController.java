package com.marta.logistika.controller;

import com.marta.logistika.dto.MonitorDataDTO;
import com.marta.logistika.dto.OrderEntryForm;
import com.marta.logistika.dto.OrderEntryResponse;
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
import org.springframework.web.bind.annotation.*;


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
    public String showOrdersAndTicketsPage() {
        return "orders/monitor";
    }

    @GetMapping(value = "/monitor", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public MonitorDataDTO provideDataForMonitor () {

        MonitorDataDTO response = new MonitorDataDTO();
        response.setOrders(orderService.listAllUnassigned());
        response.setTickets(tripTicketService.listAllUnapproved());

        return response;
    }

    @GetMapping(value = "/add-order-to-ticket", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public MonitorDataDTO addOrderToTicket(
            @RequestParam long orderId,
            @RequestParam long ticketId) {

        tripTicketService.addOrderToTicket(tripTicketService.findById(ticketId), orderService.findById(orderId));

        MonitorDataDTO response = new MonitorDataDTO();
        response.setOrders(orderService.listAllUnassigned());
        response.setTickets(tripTicketService.listAllUnapproved());

        return response;
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


    @PostMapping(value = "/add", consumes = {"application/x-www-form-urlencoded"})
    public ResponseEntity newOrderFormProcessing(OrderEntryResponse orderEntryResponse) {
        OrderEntryForm order = new OrderEntryForm();
        order.setDescription(orderEntryResponse.getDescription());
        order.setWeight(orderEntryResponse.getWeight());
        order.setFromCity(cityService.findById(orderEntryResponse.getFromCity()));
        order.setToCity(cityService.findById(orderEntryResponse.getToCity()));
        orderService.add(order);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
