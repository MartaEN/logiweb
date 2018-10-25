package com.marta.logistika.dao.impl;

import com.marta.logistika.dto.OrderStatsLine;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.dao.api.OrderDao;
import org.springframework.stereotype.Repository;

import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public long count() {
        return em.createQuery("SELECT COUNT (o) FROM OrderEntity o",
                Long.class)
                .getSingleResult();
    }

    @Override
    public List<OrderStatsLine> getUnassignedOrdersByRoute() {
        return em.createQuery(
                "SELECT NEW com.marta.logistika.dto.OrderStatsLine(o.fromCity, o.toCity, COUNT(o), SUM(o.weight)) FROM OrderEntity o " +
                        "WHERE o.status='NEW'" +
                        "GROUP BY o.fromCity, o.toCity " +
                        "ORDER BY SUM(o.weight) DESC",
                OrderStatsLine.class)
                .getResultList();
    }

    @Override
    public List<OrderStatsLine> getUnassignedOrdersByRouteForDate(LocalDate date) {
        return em.createQuery(
                "SELECT NEW com.marta.logistika.dto.OrderStatsLine(o.fromCity, o.toCity, COUNT(o), SUM(o.weight)) FROM OrderEntity o " +
                        "WHERE o.status='NEW' AND o.creationDate BETWEEN :start AND :finish " +
                        "GROUP BY o.fromCity, o.toCity " +
                        "ORDER BY SUM(o.weight) DESC",
                OrderStatsLine.class)
                .setParameter("start", date.atStartOfDay())
                .setParameter("finish", date.atTime(LocalTime.MAX))
                .getResultList();
    }

    @Override
    public List<OrderEntity> listUnassigned(long fromCityId, long toCityId) {
        return em.createQuery(
                "SELECT o FROM OrderEntity o " +
                        "WHERE o.status='NEW' AND o.fromCity.id=:fromCityId AND o.toCity.id=:toCityId " +
                        "ORDER BY o.weight DESC",
                OrderEntity.class)
                .setParameter("fromCityId", fromCityId)
                .setParameter("toCityId", toCityId)
                .getResultList();
    }

    @Override
    public List<OrderEntity> listUnassigned(long fromCityId, long toCityId, LocalDate date) {
        return em.createQuery(
                "SELECT o FROM OrderEntity o " +
                        "WHERE o.status='NEW' AND o.fromCity.id=:fromCityId AND o.toCity.id=:toCityId " +
                        " AND o.creationDate BETWEEN :start AND :finish " +
                        "ORDER BY o.weight DESC",
                OrderEntity.class)
                .setParameter("fromCityId", fromCityId)
                .setParameter("toCityId", toCityId)
                .setParameter("start", date.atStartOfDay())
                .setParameter("finish", date.atTime(LocalTime.MAX))
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
    public List<LocalDateTime> getDatesOfUnassignedOrders() {
        return em.createQuery(
                "SELECT o.creationDate FROM OrderEntity o " +
                        "WHERE o.status='NEW' " +
                        "GROUP BY o.creationDate " +
                        "ORDER BY o.creationDate ASC",
                LocalDateTime.class)
                .getResultList();
    }

}
