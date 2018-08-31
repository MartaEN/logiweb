package com.marta.logistika.service;

import com.marta.logistika.exception.EntityNotFoundException;
import com.marta.logistika.model.City;
import com.marta.logistika.model.Order;
import com.marta.logistika.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(@Qualifier("orderRepository") OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Iterable<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(orderId, Order.class));
    }

}
