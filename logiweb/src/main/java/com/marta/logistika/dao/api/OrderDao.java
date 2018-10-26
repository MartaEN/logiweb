package com.marta.logistika.dao.api;

import com.marta.logistika.dto.OrderStatsLine;
import com.marta.logistika.entity.OrderEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao extends GenericDao<OrderEntity> {

    OrderEntity findById(long id);

    long count();

    List<OrderEntity> listAllUnassigned();

    List<OrderStatsLine> getUnassignedOrdersByRoute();

    List<OrderStatsLine> getUnassignedOrdersByRouteForDate(LocalDate date);

    List<OrderEntity> listUnassigned(long fromCityId, long toCityId);

    List<OrderEntity> listUnassigned(long fromCityId, long toCityId, LocalDate date);

    List<OrderEntity> getOrdersPage(int index, int maxRecordsOnPage);

    List<LocalDateTime> getDatesOfUnassignedOrders();

}
