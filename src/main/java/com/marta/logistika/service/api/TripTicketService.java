package com.marta.logistika.service.api;

import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.exception.ServiceException;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface TripTicketService {

    void createTicket(String truckRegNum, LocalDateTime date, @Nullable CityEntity toCity);
    void addOrderToTicket(long ticketId, long orderId) throws ServiceException;
    void removeOrderFromTicket(long ticketId, long orderId);
    void signTicket(long ticketId) throws ServiceException;
    void deleteTicket(long ticketId);

    TripTicketEntity findById(long ticketId);
    TripTicketRecord findDtoById(long ticketId);
    List<TripTicketRecord> listAllUnapproved();

    Map<YearMonth, Long> getPlannedMinutesByYearMonth(TripTicketEntity ticket);
    List<OrderEntity> listAllOrderInTicket(long id);

}
