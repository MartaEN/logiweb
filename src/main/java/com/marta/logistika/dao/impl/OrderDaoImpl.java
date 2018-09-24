package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<OrderEntity> getOrdersPage(int index, int maxRecordsOnPage) {
        return em.createQuery(
                "SELECT o FROM OrderEntity o ORDER BY o.id DESC",
                OrderEntity.class)
                .setFirstResult(index)
                .setMaxResults(maxRecordsOnPage)
                .getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT (o) FROM OrderEntity o",
                Long.class)
                .getSingleResult();
    }
}
