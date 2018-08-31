package com.marta.logistika;

import com.marta.logistika.configuration.ApplicationConfiguration;
import com.marta.logistika.model.City;
import com.marta.logistika.model.Order;
import com.marta.logistika.service.CityService;
import com.marta.logistika.service.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collection;


public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        CityService cityService = ctx.getBean("cityService", CityService.class);
        OrderService orderService = ctx.getBean("orderService", OrderService.class);

        City moscow = new City();
        moscow.setName("Moscow");
        cityService.addCity(moscow);

        Order testOrder = new Order();
        testOrder.setDescription("test order");
        testOrder.setFromCity(moscow);
        testOrder.setToCity(moscow);
        testOrder.setWeight(100f);
        orderService.addOrder(testOrder);

        Iterable<Order> orders = orderService.getOrders();
        orders.forEach(order -> {
            System.out.println("ID: " + order.getId());
            System.out.println("Description: " + order.getDescription());
            System.out.println("Weight: " + order.getWeight());
            System.out.println("Going from: " + order.getFromCity().getName());
            System.out.println("Going from: " + order.getToCity().getName());
        });

    }
}
