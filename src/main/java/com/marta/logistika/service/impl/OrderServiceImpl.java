package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.dto.OrderEntryForm;
import com.marta.logistika.dto.OrderRecordFull;
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
    private final static int ROWS_PER_PAGE = 6;

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
    public OrderRecordFull findById(long id) {
        return mapper.map(orderDao.findById(id), OrderRecordFull.class);
    }

    @Override
    public List<OrderRecordShort> listAllUnassigned() {
        return orderDao.listAllUnassigned().stream()
                .map(o -> mapper.map(o, OrderRecordShort.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderRecordShort> getOrdersPage(int page) {
        int index = (page - 1) * ROWS_PER_PAGE;
        return orderDao.getOrdersPage(index, ROWS_PER_PAGE).stream()
                .map(o -> mapper.map(o, OrderRecordShort.class))
                .collect(Collectors.toList());
    }

    @Override
    public int countPages() {
        long rowCount = orderDao.count();
        int pageCount = (int) Math.ceil ( (float) rowCount / ROWS_PER_PAGE );
        return pageCount;
    }
}
