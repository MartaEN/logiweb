package com.marta.logistika.service.api;

import com.marta.logistika.entity.OrderEntity;

public interface OrderService {

    void add (OrderEntity order);
    OrderEntity findById(long id);

}
