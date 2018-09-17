package com.marta.logistika.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("LOAD")
public class TransactionLoadEntity extends TransactionEntity {

    public TransactionLoadEntity() {}

    public TransactionLoadEntity(OrderEntity order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "TransactionLoadEntity{" +
                "order id =" + order.getId() +
                '}';
    }
}
