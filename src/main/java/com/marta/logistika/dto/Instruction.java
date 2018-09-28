package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;

import java.util.List;

public class Instruction {

    private TripTicketRecord ticket;
    private int targetStep;
    private Command command;
    private String directiveMessage;
    private String requestedActionMessage;
    private CityEntity currentStop;
    private List<OrderRecordDriverInstruction> orders;

    public enum Command {
        GOTO ("goto"),
        LOAD ("load"),
        UNLOAD ("unload"),
        FINISH ("finish");

        private String path;
        Command(String path) { this.path = path; }
        public String getPath() { return path; }
    }

    public TripTicketRecord getTicket() {
        return ticket;
    }

    public void setTicket(TripTicketRecord ticket) {
        this.ticket = ticket;
    }

    public int getTargetStep() {
        return targetStep;
    }

    public void setTargetStep(int targetStep) {
        this.targetStep = targetStep;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getDirectiveMessage() {
        return directiveMessage;
    }

    public void setDirectiveMessage(String directiveMessage) {
        this.directiveMessage = directiveMessage;
    }

    public String getRequestedActionMessage() {
        return requestedActionMessage;
    }

    public void setRequestedActionMessage(String requestedActionMessage) {
        this.requestedActionMessage = requestedActionMessage;
    }

    public CityEntity getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(CityEntity currentStop) {
        this.currentStop = currentStop;
    }

    public List<OrderRecordDriverInstruction> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderRecordDriverInstruction> orders) {
        this.orders = orders;
    }
}
