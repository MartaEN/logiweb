package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.dto.OrderEntryForm;
import com.marta.logistika.dto.OrderRecordShort;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service ("orderService")
public class OrderServiceImpl extends AbstractService implements OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    @Transactional
    public void add(OrderEntryForm order) {
        orderDao.add(mapper.map(order, OrderEntity.class));
    }

    @Override
    public OrderEntity findById(long id) {
        return orderDao.findById(id);
    }

    @Override
    public List<OrderRecordShort> listAllUnassigned() {
        return orderDao.listAllUnassigned().stream()
                .map(o -> mapper.map(o, OrderRecordShort.class))
                .collect(Collectors.toList());
    }
}
