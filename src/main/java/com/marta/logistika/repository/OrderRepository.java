package com.marta.logistika.repository;

import com.marta.logistika.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("orderRepository")
public interface OrderRepository  extends JpaRepository<Order, Long>  {

    @Query("select o from Order o where o.id = :id")
    Optional<Order> findById(@Param("id") long orderId);

}
