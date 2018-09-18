package com.marta.logistika.service.api;

import com.marta.logistika.dto.OrderEntryForm;
import com.marta.logistika.entity.OrderEntity;

import java.util.List;

public interface OrderService {

    void add (OrderEntryForm order);
    OrderEntity findById(long id);
    List<OrderEntity> listAllUnassigned();
}
