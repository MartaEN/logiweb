package com.marta.logistika.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("UNLOAD")
public class TransactionUnloadEntity extends TransactionEntity {

    public TransactionUnloadEntity() {
    }

    public TransactionUnloadEntity(OrderEntity order) {
        this.order = order;
    }

}
