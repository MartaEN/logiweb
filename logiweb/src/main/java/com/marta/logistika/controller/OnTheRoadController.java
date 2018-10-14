package com.marta.logistika.controller;

import com.marta.logistika.dto.Instruction;
import com.marta.logistika.service.api.TripTicketService;
import com.marta.logistika.dto.InstructionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/logiweb")
public class OnTheRoadController {

    private final TripTicketService ticketService;

    @Autowired
    public OnTheRoadController(TripTicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Renders core drivers' page
     * @return path to jsp
     */
    @GetMapping
    public String home(){
        return "drivers/view";
    }

    /**
     * @return JSON current instruction to the requesting driver
     */
    @GetMapping(value = "/instruction", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction getInstruction(Principal principal) {
        return ticketService.getInstructionForDriver(principal);
    }

    /**
     * Record the message from the driver that the truck has reached the next stopover
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/goto", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction goTo(Principal principal, @ModelAttribute InstructionDetails instruction) {
        ticketService.reachStopover(principal, instruction.getTicketId(), instruction.getTargetStep());
        return getInstruction(principal);
    }

    /**
     * Record the message from the driver that the load has been completed
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction load(Principal principal, @ModelAttribute InstructionDetails instruction) {
        ticketService.loadAtStopover(principal, instruction.getTicketId(), instruction.getTargetStep());
        return getInstruction(principal);
    }

    /**
     * Record the message from the driver that the unload has been completed
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/unload", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction unload(Principal principal, @ModelAttribute InstructionDetails instruction) {
        ticketService.unloadAtStopover(principal, instruction.getTicketId(), instruction.getTargetStep());
        return getInstruction(principal);
    }

    /**
     * Record the message from the driver that his shift is over
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/finish", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction finish(Principal principal, @ModelAttribute InstructionDetails instruction) {
        return getInstruction(principal);
    }
}
