package com.marta.logistika.controller;

import com.marta.logistika.dto.OrderEntryForm;
import com.marta.logistika.dto.OrderRecordFull;
import com.marta.logistika.dto.OrderStatsResponse;
import com.marta.logistika.dto.SystemMessage;
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

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;


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
     * Returns core "/orders" page with unassigned orders and unapproved tickets to be processed
     *
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @GetMapping
    public String showOrdersAndTicketsPage(Model uiModel) {

        uiModel.addAttribute("orderEntryForm", new OrderEntryForm());
        uiModel.addAttribute("cities", cityService.listAll());

        return "office/orders/monitor";
    }

    /**
     * Returns information on new (unassigned) orders - limited with additional filter if provided
     *
     * @param fromCityId departure city
     * @param toCityId   destination city
     * @param date       order creation date
     * @return JSON response
     */
    @GetMapping(value = "/view/unassigned", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public OrderStatsResponse showStatistics(@RequestParam(name = "fromCity") String fromCityId,
                                             @RequestParam(name = "toCity") String toCityId,
                                             @RequestParam(name = "date") String date) {
        if (!Objects.equals(fromCityId, "all") && !Objects.equals(toCityId, "all")) {
            if (Objects.equals(date, "all"))
                return orderService.getUnassignedOrders(Long.parseLong(fromCityId), Long.parseLong(toCityId), null);
            else
                return orderService.getUnassignedOrders(Long.parseLong(fromCityId), Long.parseLong(toCityId), LocalDate.parse(date));
        } else {
            if (Objects.equals(date, "all")) return orderService.getUnassignedOrdersSummary();
            else return orderService.getUnassignedOrdersSummary(LocalDate.parse(date));
        }
    }

    /**
     * Tries to add order to a trip ticket and returns updated data for the "/orders" page
     *
     * @param locale   user locale
     * @param orderId  order to be added
     * @param ticketId ticket to accept the order
     * @return JSON data with unassigned orders and unapproved tickets in process
     * and an error message in case the operation fails
     */
    @GetMapping(value = "/add-single-order-to-ticket", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public SystemMessage addOrderToTicket(
            Locale locale,
            @RequestParam long orderId,
            @RequestParam long ticketId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("###LOGIWEB### User %s: Adding Single Order %d to Ticket %d",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    orderId, ticketId));
        }
        return tripTicketService.addSingleOrderToTicketAndReport(ticketId, orderId, locale);
    }

    /**
     * Tries to add several orders to a trip ticket and returns updated data for the "/orders" page
     */
    @GetMapping(value = "/add-multiple-orders-to-ticket", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public SystemMessage addOrdersToTicket(
            @RequestParam(name = "fromCity") long fromCityId,
            @RequestParam(name = "toCity") long toCityId,
            @RequestParam(name = "date") String date,
            @RequestParam(name = "ticketId") long ticketId,
            Locale locale) {
        LocalDate parsedDate;
        if (date.equals("all")) parsedDate = null;
        else parsedDate = LocalDate.parse(date);
        return tripTicketService.addMultipleOrdersToTicketAndReport(fromCityId, toCityId, parsedDate, ticketId, locale);
    }

    /**
     * Produces order form to enter new order
     *
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @GetMapping(value = "/add-no-ajax")
    public String newOrderForm(Model uiModel) {

        uiModel.addAttribute("orderEntryForm", new OrderEntryForm());
        uiModel.addAttribute("cities", cityService.listAll());
        return "office/orders/add-no-ajax";
    }

    /**
     * Persists new order as per form filled by user
     *
     * @param orderEntryForm order entry form data
     * @param bindingResult  validation result
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

        LOGGER.info("###LOGIWEB### User {}: Created new Order id {}",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                newOrderId);

        return "redirect:/orders";
    }

    /**
     * Produces webpage with paginated list of orders
     *
     * @param page    number of page to be shown
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @GetMapping(value = "/view")
    public String viewNumberedPage(@RequestParam("page") int page, Model uiModel) {

        long totalPageCount = orderService.countPages();
        if (totalPageCount == 0) {
            return "office/orders/list";
        }
        if (page > orderService.countPages() || page < 1) {
            return "redirect:/orders/view?page=1";
        }

        uiModel.addAttribute("orders", orderService.getOrdersPage(page));
        uiModel.addAttribute("page", page);
        uiModel.addAttribute("totalPages", totalPageCount);

        return "office/orders/list";
    }

    /**
     * Returns info on selected order
     *
     * @param id order id
     * @return JSON data with order record
     */
    @GetMapping(value = "/show-order")
    @ResponseBody
    public OrderRecordFull showOrder(@RequestParam("id") long id) {
        return orderService.findById(id);
    }
}
