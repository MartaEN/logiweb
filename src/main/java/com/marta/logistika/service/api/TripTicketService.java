package com.marta.logistika.service.api;

import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.service.ServiceException;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface TripTicketService {

    void createTripTicket (String truckRegNum, LocalDateTime date, @Nullable CityEntity toCity);
    void signTicket(long ticketId);
    void approveTripTicket (long id);
    TripTicketEntity findById(long id);
    TripTicketRecord findDtoById (long id);
    void addOrderToTicket (TripTicketEntity ticket, OrderEntity order) throws ServiceException;
    void removeOrderFromTicket (TripTicketEntity ticket, OrderEntity order);
    int getDistance(TripTicketEntity ticket);
    Map<YearMonth, Long> getPlannedMinutesByYearMonth(TripTicketEntity ticket);
    List<TripTicketRecord> listAllUnapproved();
    List<OrderEntity> listAllOrderInTicket(long id);

}
