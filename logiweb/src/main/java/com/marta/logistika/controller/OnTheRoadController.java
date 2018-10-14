package com.marta.logistika.controller;

import com.marta.logistika.dto.Instruction;
import com.marta.logistika.service.api.DriverService;
import com.marta.logistika.service.api.TripTicketService;
import com.marta.logistika.dto.InstructionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
    public Instruction getInstruction() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String personalId = driverService.findPersonalIdByUsername(username);
        return ticketService.getInstructionForDriver(personalId);
    }

    /**
     * Record the message from the driver that the truck has reached the next stopover
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/goto", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction goTo(@ModelAttribute InstructionDetails instruction) {
        ticketService.reachStopover(instruction.getTicketId(), instruction.getTargetStep());
        return getInstruction();
    }

    /**
     * Record the message from the driver that the load has been completed
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction load(@ModelAttribute InstructionDetails instruction) {
        ticketService.loadAtStopover(instruction.getTicketId(), instruction.getTargetStep());
        return getInstruction();
    }

    /**
     * Record the message from the driver that the unload has been completed
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/unload", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction unload(@ModelAttribute InstructionDetails instruction) {
        ticketService.unloadAtStopover(instruction.getTicketId(), instruction.getTargetStep());
        return getInstruction();
    }

    /**
     * Record the message from the driver that his shift is over
     * @param instruction InstructionDetails
     * @return JSON next instruction to the driver
     */
    @PostMapping(value = "/finish", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Instruction finish(@ModelAttribute InstructionDetails instruction) {
        return getInstruction();
    }
}
