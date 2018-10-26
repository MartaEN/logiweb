package com.marta.logistika.controller;

import com.marta.logistika.dto.Instruction;
import com.marta.logistika.dto.InstructionDetails;
import com.marta.logistika.service.api.TripTicketService;
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
     *
     * @return path to jsp
     */
    @GetMapping
    public String home() {
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
     * Record the message from the driver that he is going online (starting his shift)
     *
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/start", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction goOnline(Principal principal, @ModelAttribute InstructionDetails instruction) {
        ticketService.setOnline(principal, instruction.getTicketId());
        return getInstruction(principal);
    }

    /**
     * Record the message from the driver that the truck has reached the next stopover
     *
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
     *
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
     * Records the message from the driver that the unload has been completed
     *
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
     * Sets the requesting driver as the driving one
     *
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/first-driver", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction setFirstDriver(Principal principal, @ModelAttribute InstructionDetails instruction) {
        ticketService.setFirstDriver(principal, instruction.getTicketId());
        return getInstruction(principal);
    }

    /**
     * Records the message from the driver that a road break is being made
     *
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/road-break", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction startRoadBreak(Principal principal, @ModelAttribute InstructionDetails instruction) {
        ticketService.startRoadBreak(principal, instruction.getTicketId());
        return getInstruction(principal);
    }

    /**
     * Records the message from the driver that a road break is over
     *
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/road-break-over", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction finishRoadBreak(Principal principal, @ModelAttribute InstructionDetails instruction) {
        ticketService.finishRoadBreak(principal, instruction.getTicketId());
        return getInstruction(principal);
    }

    /**
     * Records the message from the driver he is making a stopover break
     *
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/stopover-break", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction makeStopoverBreak(Principal principal) {
        ticketService.startStopoverBreak(principal);
        return getInstruction(principal);
    }

    /**
     * Records the message from the driver that his stopover break is over
     *
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/stopover-break-over", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction finishBreak(Principal principal, @ModelAttribute InstructionDetails instruction) {
        ticketService.finishStopoverBreak(principal, instruction.getTicketId());
        return getInstruction(principal);
    }

    /**
     * Record the message from the driver that his shift is over
     *
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/finish", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction finish(Principal principal, @ModelAttribute InstructionDetails instruction) {
        return getInstruction(principal);
    }
}
