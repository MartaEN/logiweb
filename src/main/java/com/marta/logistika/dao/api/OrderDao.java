package com.marta.logistika.dao.api;

import com.marta.logistika.entity.OrderEntity;

import java.util.List;

public interface OrderDao extends GenericDao<OrderEntity> {

    OrderEntity findById (long id);
    List<OrderEntity> listAllUnassigned();
    List<OrderEntity> getOrdersPage(int index, int maxRecordsOnPage);
    long count();
}
