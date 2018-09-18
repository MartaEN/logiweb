package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.marta.logistika.enums.OrderStatus.NEW;

@Repository("orderRepository")
public class OrderDaoImpl extends AbstractDao<OrderEntity> implements OrderDao {

    @Override
    public OrderEntity findById(long id) {
        return em.find(OrderEntity.class, id);
    }

    @Override
    public List<OrderEntity> listAllUnassigned() {
        return em.createQuery(
                "SELECT o FROM OrderEntity o WHERE o.status='NEW'",
                OrderEntity.class)
                .getResultList();
    }
}
