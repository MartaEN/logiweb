package com.marta.logistika.entity;

import com.marta.logistika.enums.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class OrderEntity extends AbstractEntity {

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int weight;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "fromCity")
    private CityEntity fromCity;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "toCity")
    private CityEntity toCity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @PrePersist
    public void setDefaults() {
        creationDate = LocalDateTime.now();
        status = OrderStatus.NEW;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public CityEntity getFromCity() {
        return fromCity;
    }

    public void setFromCity(CityEntity fromCity) {
        this.fromCity = fromCity;
    }

    public CityEntity getToCity() {
        return toCity;
    }

    public void setToCity(CityEntity toCity) {
        this.toCity = toCity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id='" + id +
                ", created on =" + creationDate +
                ", description='" + description + '\'' +
                ", weight=" + weight +
                ", fromCity=" + fromCity.getName() +
                ", toCity=" + toCity.getName() +
                ", status=" + status +
                '}';
    }
}
