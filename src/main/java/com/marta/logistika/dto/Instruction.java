package com.marta.logistika.dto;

import java.util.List;

public class Instruction {

    private long ticketId;
    private int step;
    private Command command;
    private String directiveMessage;
    private String requestedActionMessage;
    private List<OrderRecordFull> orders;

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

    public List<OrderRecordFull> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderRecordFull> orders) {
        this.orders = orders;
    }
}
