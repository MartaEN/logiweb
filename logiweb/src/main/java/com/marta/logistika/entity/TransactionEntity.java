package com.marta.logistika.entity;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "transactions")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 6)
@DiscriminatorOptions(force = true)
public abstract class TransactionEntity extends AbstractEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    protected OrderEntity order;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
