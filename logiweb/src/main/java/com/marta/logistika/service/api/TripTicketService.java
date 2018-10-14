package com.marta.logistika.service.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.exception.NoDriversAvailableException;
import com.marta.logistika.exception.PastDepartureDateException;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.dto.Instruction;
import org.springframework.lang.Nullable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface TripTicketService {

    // creating, filling and approving the ticket (office operations)
    long createTicket(String truckRegNum, LocalDateTime date, @Nullable CityEntity toCity);
    void updateDepartureDateTime(long ticketId, LocalDateTime departureDateTime) throws ServiceException;
    void addOrderToTicket(long ticketId, long orderId) throws ServiceException;
    void removeOrderFromTicket(long ticketId, long orderId);
    void approveTicket(long ticketId) throws PastDepartureDateException, NoDriversAvailableException;
    void deleteTicket(long ticketId);

    // processing ticket on the road (driver operations)
    Instruction getInstructionForDriver(Principal principal);
    void reachStopover(Principal principal, long ticketId, int step);
    void loadAtStopover(Principal principal, long ticketId, int step);
    void unloadAtStopover(Principal principal, long ticketId, int step);

    // other very useful operations
    TripTicketRecord findById(long ticketId);
    List<TripTicketRecord> listAll();
    List<TripTicketRecord> listAllUnapproved();
    List<OrderEntity> listAllOrderInTicket(long id);
    Map<YearMonth, Long> getPlannedMinutesByYearMonth(TripTicketEntity ticket);
    LocalDateTime getMinNewDateTime(long ticketId);

}
