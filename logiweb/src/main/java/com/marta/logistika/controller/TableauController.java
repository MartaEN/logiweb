package com.marta.logistika.controller;

import com.marta.logistika.dto.OrderRecordShort;
import com.marta.logistika.service.api.DriverService;
import com.marta.logistika.service.api.OrderService;
import com.marta.logistika.service.api.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/tableau")
public class TableauController {

    private final OrderService orderService;
    private final DriverService driverService;
    private final TruckService truckService;

    @Autowired
    public TableauController(OrderService orderService, DriverService driverService, TruckService truckService) {
        this.orderService = orderService;
        this.driverService = driverService;
        this.truckService = truckService;
    }

    @GetMapping(value = "/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<OrderRecordShort> getListOfLatestOrders() {
        return orderService.getOrdersPage(1);
    }

    @GetMapping(value = "/stats/drivers", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public LinkedHashMap<String, Integer> getDriverStats() {
        return driverService.getDriverStatistics();
    }

    @GetMapping(value = "/stats/trucks", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public LinkedHashMap<String, Long> getTruckStats() {
        return truckService.getTruckStatistics();
    }

}
