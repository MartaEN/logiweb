package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.enums.DriverStatus;

import java.util.List;

public class Instruction {

    private TripTicketRecord ticket;
    private int targetStep;
    private Task task;
    private String url;
    private String directiveMessage;
    private String requestedActionMessage;
    private CityEntity currentStop;
    private List<OrderRecordDriverInstruction> orders;
    private DriverStatus driverStatus;

    public enum Task {
        START ("start"),
        GOTO ("goto"),
        LOAD ("load"),
        UNLOAD ("unload"),
        FINISH_STOPOVER_BREAK ("stopover-break-over"),
        FINISH_ROAD_BREAK("road-break-over"),
        CLOSE_TICKET("finish"),
        NONE ("none");

        private String url;
        Task(String url) { this.url = url; }
        public String getUrl() { return url; }
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }
}
