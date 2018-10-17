package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.dto.OrderRecordShort;
import com.marta.logistika.dto.TableauData;
import com.marta.logistika.messaging.MessageSender;
import com.marta.logistika.service.api.TableauService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service("tableauService")
public class TableauServiceImpl extends AbstractService implements TableauService {

    private final MessageSender messageSender;
    private final OrderDao orderDao;

    @Autowired
    public TableauServiceImpl(MessageSender messageSender, OrderDao orderDao) {
        this.messageSender = messageSender;
        this.orderDao = orderDao;
    }

    @Override
    public void updateTableau() {
        TableauData data = new TableauData();
        data.setOrders(orderDao.getOrdersPage(1, 10).stream()
                .map(o -> mapper.map(o, OrderRecordShort.class))
                .collect(Collectors.toList())
        );
        //todo
        messageSender.sendMessage(data);
    }


}
