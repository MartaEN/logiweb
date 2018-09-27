package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;

import java.util.List;

public class Instruction {

    private long ticketId;
    private int step;
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

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
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
