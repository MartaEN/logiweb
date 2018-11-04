package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.dto.*;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.event.EntityUpdateEvent;
import com.marta.logistika.exception.checked.NoRouteFoundException;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.service.api.OrderService;
import com.marta.logistika.service.api.RoadService;
import com.marta.logistika.service.api.TableauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("orderService")
public class OrderServiceImpl extends AbstractService implements OrderService {

    private final OrderDao orderDao;
    private final RoadService roadService;
    private final CityService cityService;
    private final TableauService tableauService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private static final int ROWS_PER_PAGE = 10;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, RoadService roadService, CityService cityService, TableauService tableauService, ApplicationEventPublisher applicationEventPublisher) {
        this.orderDao = orderDao;
        this.roadService = roadService;
        this.cityService = cityService;
        this.tableauService = tableauService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * persists new order
     *
     * @param order new order
     * @return persisted order id
     */
    @Override
    @Transactional
    public long add(OrderEntryForm order) {
        long newOrderId = orderDao.add(mapper.map(order, OrderEntity.class));
        tableauService.updateTableau();
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
        return newOrderId;
    }

    /**
     * Returns order record
     *
     * @param id order id
     * @return order record
     */
    @Override
    public OrderRecordFull findById(long id) {
        return mapper.map(orderDao.findById(id), OrderRecordFull.class);
    }

    /**
     * Prepares statistics on all unassigned orders - by destination, total distance, number of orders, total weight
     *
     * @return list of order statistics lines
     */
    @Override
    public OrderStatsResponse getUnassignedOrdersSummary() {
        OrderStatsResponse response = new OrderStatsResponse();
        response.setResponseType(OrderStatsResponse.ResponseType.SUMMARY);
        response.setDateFilter("all");

        List<OrderStatsLine> orderStatsLines = orderDao.getUnassignedOrdersByRoute();
        orderStatsLines.forEach(line -> {
            try {
                line.setDistance(roadService.getDistanceFromTo(line.getFromCity(), line.getToCity()));
            } catch (NoRouteFoundException e) {
                line.setDistance(-1L);
            }
        });
        response.setOrderLines(orderStatsLines);

        List<String> openDates = orderDao.getDatesOfUnassignedOrders().stream()
                .map(d -> d.toLocalDate().toString())
                .distinct()
                .collect(Collectors.toList());
        response.setOpenDates(openDates);

        return response;
    }

    @Override
    public OrderStatsResponse getUnassignedOrdersSummary(LocalDate date) {
        OrderStatsResponse response = new OrderStatsResponse();
        response.setResponseType(OrderStatsResponse.ResponseType.SUMMARY);
        response.setDateFilter(date.toString());

        List<OrderStatsLine> orderStatsLines = orderDao.getUnassignedOrdersByRouteForDate(date);
        orderStatsLines.forEach(line -> {
            try {
                line.setDate(date.toString());
                line.setDistance(roadService.getDistanceFromTo(line.getFromCity(), line.getToCity()));
            } catch (NoRouteFoundException e) {
                line.setDistance(-1L);
            }
        });
        response.setOrderLines(orderStatsLines);

        List<String> openDates = orderDao.getDatesOfUnassignedOrders().stream()
                .map(d -> d.toLocalDate().toString())
                .distinct()
                .collect(Collectors.toList());
        response.setOpenDates(openDates);

        return response;
    }

    /**
     * Lists all unassigned orders requested to be transported between two specified cities
     *
     * @param fromCityId departure city id
     * @param toCityId   destination city id
     * @param date       filter date - if requested
     * @return response object containing requested order list and request parameters
     */
    @Override
    public OrderStatsResponse getUnassignedOrders(long fromCityId, long toCityId, @Nullable LocalDate date) {
        OrderStatsResponse response = new OrderStatsResponse();
        response.setResponseType(OrderStatsResponse.ResponseType.DRILLDOWN);
        if (date != null) response.setDateFilter(date.toString());

        List<OrderStatsLine> orderStatsLines = new ArrayList<>();

        CityEntity fromCity = cityService.findById(fromCityId);
        CityEntity toCity = cityService.findById(toCityId);
        response.setCitiesFilter(String.format("%s-%s", fromCity.getName(), toCity.getName()));
        response.setCitiesFilterParams(String.format("fromCity=%d&toCity=%d", fromCityId, toCityId));

        int distance = 0;
        try {
            distance = roadService.getDistanceFromTo(fromCity, toCity);
        } catch (NoRouteFoundException e) {
            // ok to show zero distance for tableau in case of no route
        }

        List<OrderEntity> orders = orderDao.listUnassigned(fromCityId, toCityId);
        for (OrderEntity order : orders) {
            orderStatsLines.add(new OrderStatsLine(fromCity, toCity, order.getId(), order.getCreationDate().toLocalDate().toString(), order.getWeight(), distance));
        }
        response.setOrderLines(orderStatsLines);

        List<String> openDates = orders.stream()
                .map(OrderEntity::getCreationDate)
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .sorted()
                .map(LocalDate::toString)
                .collect(Collectors.toList());
        response.setOpenDates(openDates);

        return response;
    }


    /**
     * Returns one page of order list
     *
     * @param page page number
     * @return order list - one page
     */
    @Override
    @Transactional
    public List<OrderRecordShort> getOrdersPage(int page) {
        int index = (page - 1) * ROWS_PER_PAGE;
        return orderDao.getOrdersPage(index, ROWS_PER_PAGE).stream()
                .map(o -> mapper.map(o, OrderRecordShort.class))
                .collect(Collectors.toList());
    }

    /**
     * @return number of pages needed to show full orders list
     */
    @Override
    public int countPages() {
        long rowCount = orderDao.count();
        return (int) Math.ceil((float) rowCount / ROWS_PER_PAGE);
    }
}
