package com.marta.logistika.service.api;

import com.marta.logistika.dto.OrderEntryForm;
import com.marta.logistika.dto.OrderRecordFull;
import com.marta.logistika.dto.OrderRecordShort;
import com.marta.logistika.entity.OrderEntity;

import java.util.List;

public interface OrderService {

    long add (OrderEntryForm order);
    OrderRecordFull findById(long id);
    List<OrderRecordShort> listAllUnassigned();
    List<OrderRecordShort> getOrdersPage(int page);
    int countPages();
}
