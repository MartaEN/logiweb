package com.marta.logistika.controller;

import com.marta.logistika.dto.*;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.OrderService;
import com.marta.logistika.service.api.TripTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;


@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;
    private final TripTicketService tripTicketService;
    private final CityService cityService;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);


    @Autowired
    public OrdersController(OrderService orderService, TripTicketService tripTicketService, CityService cityService) {
        this.orderService = orderService;
        this.tripTicketService = tripTicketService;
        this.cityService = cityService;
    }

    /**
     * Returns core "/orders" page with unassigned orders and unapproved tickets in process
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @GetMapping
    public String showOrdersAndTicketsPage(Model uiModel) {

        uiModel.addAttribute("orderEntryForm",  new OrderEntryForm());
        uiModel.addAttribute("cities", cityService.listAll());

        return "office/orders/monitor";
    }

    /**
     * Produces data to be shown on core "/orders" page
     * @return JSON data with unassigned orders and unapproved tickets in process
     */
    @GetMapping(value = "/monitor", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public MonitorDataResponse provideDataForMonitor () {

        MonitorDataResponse response = new MonitorDataResponse();
        response.setOrders(orderService.listAllUnassigned());
        response.setTickets(tripTicketService.listAllUnapproved());

        return response;
    }

    /**
     * Tries to add order to a trip ticket and returns updated data for the "/orders" page
     * @param locale user locale
     * @param orderId order to be added
     * @param ticketId ticket to accept the order
     * @return JSON data with unassigned orders and unapproved tickets in process
     * and an error message in case the operation fails
     */
    @GetMapping(value = "/add-order-to-ticket", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public MonitorDataResponse addOrderToTicket(
            Locale locale,
            @RequestParam long orderId,
            @RequestParam long ticketId) {

        MonitorDataResponse response = new MonitorDataResponse();

        try {
            tripTicketService.addOrderToTicket(ticketId, orderId);
            LOGGER.info(String.format("###LOGIWEB### User %s: Adding Order %d to Ticket %d - successful",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    orderId,
                    ticketId));
        } catch (ServiceException e) {
            LOGGER.error(String.format("###LOGIWEB### User %s: Adding Order %d to Ticket %d - operation failed (%s)",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    orderId,
                    ticketId,
                    e.getLocalizedMessage(Locale.ENGLISH)));
            response.setError(e.getLocalizedMessage(locale));
        }

        response.setOrders(orderService.listAllUnassigned());
        response.setTickets(tripTicketService.listAllUnapproved());

        return response;
    }
    
    /**
     * Produces order form to enter new order
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @GetMapping(value = "/add-no-ajax")
    public String newOrderForm(Model uiModel) {

        uiModel.addAttribute("orderEntryForm",  new OrderEntryForm());
        uiModel.addAttribute("cities", cityService.listAll());
        return "office/orders/add-no-ajax";
    }

    /**
     * Persists new order as per form filled by user
     * @param orderEntryForm order entry form data
     * @param bindingResult validation result
     * @return redirect to core page
     */
    @PostMapping(value = "/add-no-ajax")
    public String addNewOrder(
            @ModelAttribute OrderEntryForm orderEntryForm,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/orders/add-no-ajax";
        }

        long newOrderId = orderService.add(orderEntryForm);

        LOGGER.info(String.format("###LOGIWEB### User %s: Created new Order id %d",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                newOrderId));

        return "redirect:/orders";
    }

//    @PostMapping(value = "/add", consumes = {"application/x-www-form-urlencoded"})
//    public ResponseEntity processNewOrderForm(OrderEntryResponse orderEntryResponse) {
//        OrderEntryForm order = new OrderEntryForm();
//
//        order.setDescription(orderEntryResponse.getDescription());
//        order.setWeight(orderEntryResponse.getWeight());
//        order.setFromCity(cityService.findById(orderEntryResponse.getFromCity()));
//        order.setToCity(cityService.findById(orderEntryResponse.getToCity()));
//
//        long newOrderId = orderService.add(order);
//
//        LOGGER.info(String.format("User %s: Created new Order id %d",
//                SecurityContextHolder.getContext().getAuthentication().getName(),
//                newOrderId));
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

    /**
     * Produces webpage with paginated list of orders
     * @param page number of page to be shown
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @GetMapping (value = "/view")
    public String viewNumberedPage (@RequestParam ("page") int page, Model uiModel) {

        long totalPageCount = orderService.countPages();
        if(page > orderService.countPages() || page < 1) return "redirect:/orders/view?page=1";

        uiModel.addAttribute("orders", orderService.getOrdersPage(page));

        if(totalPageCount != 0) {
            uiModel.addAttribute("page", page);
            uiModel.addAttribute("totalPages", totalPageCount);
        }

        return "office/orders/list";
    }

    /**
     * Returns info on selected order
     * @param id order id
     * @return JSON data with order record
     */
    @GetMapping (value = "/show-order")
    @ResponseBody
    public OrderRecordFull showOrder (@RequestParam ("id") long id) {
        return orderService.findById(id);
    }
}
