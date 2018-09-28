package com.marta.logistika.controller;

import com.marta.logistika.dto.InstructionDetails;
import com.marta.logistika.service.api.DriverService;
import com.marta.logistika.service.api.TripTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/logiweb")
public class OnTheRoadController {

    private final TripTicketService ticketService;
    private final DriverService driverService;

    @Autowired
    public OnTheRoadController(TripTicketService ticketService, DriverService driverService) {
        this.ticketService = ticketService;
        this.driverService = driverService;
    }

    @GetMapping
    public String home(Model uiModel){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String personalId = driverService.findPersonalIdByUsername(username);

        uiModel.addAttribute("instruction", ticketService.getInstructionForDriver(personalId));
        uiModel.addAttribute("instructionDetails", new InstructionDetails());

        return "drivers/view";
    }

    @PostMapping(value = "/goto")
    public String goTo(@ModelAttribute InstructionDetails instruction) {
        ticketService.moveToStopover(instruction.getTicketId(), instruction.getTargetStep());
        return "redirect:/logiweb";
    }

    @PostMapping(value = "/load")
    public String load(@ModelAttribute InstructionDetails instruction) {
        ticketService.loadAtStopover(instruction.getTicketId(), instruction.getTargetStep());
        return "redirect:/logiweb";
    }

    @PostMapping(value = "/unload")
    public String unload(@ModelAttribute InstructionDetails instruction) {
        ticketService.unloadAtStopover(instruction.getTicketId(), instruction.getTargetStep());
        return "redirect:/logiweb";
    }

    @PostMapping(value = "/finish")
    public String finish(@ModelAttribute InstructionDetails instruction) {
        ticketService.closeTicket(instruction.getTicketId());
        return "redirect:/logiweb";
    }
}
