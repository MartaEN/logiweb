package com.marta.logistika.service.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.exception.checked.NoDriversAvailableException;
import com.marta.logistika.exception.checked.PastDepartureDateException;
import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.dto.Instruction;
import com.marta.logistika.exception.checked.NoRouteFoundException;
import com.marta.logistika.exception.checked.OrderDoesNotFitToTicketException;
import org.springframework.lang.Nullable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface TripTicketService {

    // creating, filling and approving the ticket (office operations)
    long createTicket(String truckRegNum, LocalDateTime date, @Nullable CityEntity toCity);
    void updateDepartureDateTime(long ticketId, LocalDateTime departureDateTime);
    void addOrderToTicket(long ticketId, long orderId) throws NoRouteFoundException, OrderDoesNotFitToTicketException;
    void removeOrderFromTicket(long ticketId, long orderId);
    void approveTicket(long ticketId) throws PastDepartureDateException, NoDriversAvailableException;
    void deleteTicket(long ticketId);

    // processing ticket on the road (driver operations)
    Instruction getInstructionForDriver(Principal principal);
    void reachStopover(Principal principal, long ticketId, int step);
    void loadAtStopover(Principal principal, long ticketId, int step);
    void unloadAtStopover(Principal principal, long ticketId, int step);
    void setFirstDriver(Principal principal, long ticketId);
    void startRoadBreak(Principal principal, long ticketId);
    void finishRoadBreak(Principal principal, long ticketId);
    void startStopoverBreak(Principal principal);
    void finishStopoverBreak(Principal principal, long ticketId);

    // other very useful operations
    TripTicketRecord findById(long ticketId);
    List<TripTicketRecord> listAll();
    List<TripTicketRecord> listAllUnapproved();
    List<OrderEntity> listAllOrderInTicket(long id);
    Map<YearMonth, Long> getPlannedMinutesByYearMonth(TripTicketEntity ticket);
    LocalDateTime getMinNewDateTime(long ticketId);

}
