package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository("orderRepository")
public class OrderDaoImpl extends AbstractDao<OrderEntity> implements OrderDao {

    @Override
    public OrderEntity findById(long id) {
        return em.find(OrderEntity.class, id);
    }
}
