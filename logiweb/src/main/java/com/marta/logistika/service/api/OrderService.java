package com.marta.logistika.service.api;

import com.marta.logistika.dto.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    long add (OrderEntryForm order);
    OrderRecordFull findById(long id);
    List<OrderRecordShort> listAllUnassigned();
    OrderStatsResponse getUnassignedOrdersSummary();
    OrderStatsResponse getUnassignedOrdersSummary(LocalDate date);
    OrderStatsResponse getUnassignedOrders(long fromCityId, long toCityId, @Nullable LocalDate date);
    List<OrderRecordShort> getOrdersPage(int page);
    int countPages();
}
