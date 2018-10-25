package com.marta.logistika.controller;

import com.marta.logistika.dto.*;
import com.marta.logistika.exception.checked.NoDriversAvailableException;
import com.marta.logistika.exception.checked.PastDepartureDateException;
import com.marta.logistika.service.api.CityService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/tickets")
public class TripTicketController {

    private final TripTicketService ticketService;
    private final CityService cityService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TripTicketController.class);

    @Autowired
    public TripTicketController(TripTicketService ticketService, CityService cityService) {
        this.ticketService = ticketService;
        this.cityService = cityService;
    }

    /**
     * Shows page with trip ticket information
     * @param id trip ticket id
     * @param uiModel data to build page
     * @return path to jsp
     */
    @GetMapping(value = "/{id}")
    public String viewTicket(@PathVariable("id") Long id, Model uiModel) {

        uiModel.addAttribute("ticket", ticketService.findById(id));
        uiModel.addAttribute("orders", ticketService.listAllOrderInTicket(id));
        uiModel.addAttribute("returnTo", "tickets/view");

        return "office/tickets/view";
    }

    /**
     * Shows page where user can view, edit and approve the selected trip ticket
     * @param ticketId ticket id
     * @param uiModel contains data to build the page
     * @return path to jsp
     */
    @GetMapping(value = "/{ticketId}/approve")
    public String viewAndApproveTicket(@PathVariable("ticketId") Long ticketId, Model uiModel) {
        prepareTicketApproveForm(ticketId, uiModel);
        return "office/tickets/approve";
    }

    /**
     * Tries to assign drivers and approve the ticket
     * @param ticketId ticket id
     * @param uiModel data to build the page
     * @param locale user locale
     * @return path to jsp
     */
    @PostMapping(value = "/{ticketId}/approve")
    public String signTicket (@PathVariable("ticketId") long ticketId, Model uiModel, Locale locale) {

        prepareTicketApproveForm(ticketId, uiModel);

        try {
            ticketService.approveTicket(ticketId);
            LOGGER.info(String.format("###LOGIWEB### User %s: Approving Ticket %d - successful",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ticketId));
            return "office/tickets/view";
        } catch (PastDepartureDateException | NoDriversAvailableException e) {
            uiModel.addAttribute("error", e.getLocalizedMessage(locale));
            LOGGER.info(String.format("###LOGIWEB### User %s: Approving Ticket %d - failed (%s)",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ticketId,
                    e.getLocalizedMessage(Locale.ENGLISH)));
            return "office/tickets/approve";
        }
    }

    /**
     * Updates departure date and time in the trip ticket edit and approval page
     * @param ticketId ticket id
     * @param futureDateTimeRecord data transfer object to request new input from user
     * @param bindingResult validation result
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @PostMapping (value = "/{ticketId}/update-departure")
    public String updateDepartureDateTime (@PathVariable("ticketId") long ticketId,
                                           @Valid @ModelAttribute FutureDateTimeRecord futureDateTimeRecord,
                                           BindingResult bindingResult,
                                           Model uiModel,
                                           Locale locale) {

        if (bindingResult.hasErrors()) {
            prepareTicketApproveForm(ticketId, uiModel);
            return "office/tickets/approve";
        }

        ticketService.updateDepartureDateTime(ticketId, futureDateTimeRecord.getDepartureDateTime());
        LOGGER.info(String.format("###LOGIWEB### User %s: Departure date time for ticket %d updated",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                ticketId));
        return String.format("redirect: /tickets/%d/approve", ticketId);
    }

    /**
     * Populates model with data
     * @param ticketId ticket id
     * @param uiModel ui model
     */
    private void prepareTicketApproveForm(long ticketId, Model uiModel) {
        uiModel.addAttribute("ticket", ticketService.findById(ticketId));
        uiModel.addAttribute("orders", ticketService.listAllOrderInTicket(ticketId));
        uiModel.addAttribute("ticketAndOrder", new TicketAndOrder());
        uiModel.addAttribute("departureDateTime", new FutureDateTimeRecord());
        uiModel.addAttribute("minNewDateTime", ticketService.getMinNewDateTime(ticketId));
    }

    /**
     * Produces trip ticket creation page
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @GetMapping(value = "/create")
    public String newTicketForm(Model uiModel) {

        uiModel.addAttribute("filterForm", new TruckFilterForm());
        uiModel.addAttribute("cities", cityService.listAll());

        return "office/tickets/create";
    }

    /**
     * Persists the ticket as submitted by user and redirects to core "/orders" page
     * @param ticketCreateForm new trip ticket data submitted by user
     * @return redirect to core "/orders" page
     */
    @PostMapping(value = "/create")
    public String newTicketCreate(@Valid @ModelAttribute TripTicketCreateForm ticketCreateForm,
                                  BindingResult bindingResult,
                                  Locale locale) {

        if(bindingResult.hasErrors()) {
            return "office/tickets/create";
        }

        long newTicketId = ticketService.createTicket(
                ticketCreateForm.getTruckRegNumber(),
                ticketCreateForm.getDepartureDateTime(),
                ticketCreateForm.getToCity() == null ? null : cityService.findById(ticketCreateForm.getToCity()));

        LOGGER.info(String.format("###LOGIWEB### User %s: Created new trip ticket id %d",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                newTicketId));

        return "redirect:/orders";
    }

    /**
     * Removes order from a trip ticket in the ticket edit and approval page
     * @param ticketAndOrder data transfer object for ticket and order id
     * @return redirect to ticket edit and approval page
     */
    @PostMapping(value = "/remove-order")
    public String removeOrderFromTicket(@ModelAttribute TicketAndOrder ticketAndOrder) {

        ticketService.removeOrderFromTicket(ticketAndOrder.getTicketId(), ticketAndOrder.getOrderId());

        LOGGER.info(String.format("###LOGIWEB### User %s: Removed order id %d from trip ticket id %d",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                ticketAndOrder.getOrderId(),
                ticketAndOrder.getTicketId()));

        return String.format("redirect:/tickets/%d/approve", ticketAndOrder.getTicketId());
    }

    /**
     * Produces trip ticket view page
     * @param uiModel data to build the page
     * @return path to jsp
     */
    @GetMapping (value = "/view")
    public String viewAllTickets (Model uiModel) {

        uiModel.addAttribute("tickets", ticketService.listAll());

        return "office/tickets/list";
    }

    /**
     * @return Returns list of unapproved tickets
     */
    @GetMapping (value = "/list/unapproved", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<TripTicketRecord> listAllUnapprovedTickets () {
        return ticketService.listAllUnapproved();
    }


}
