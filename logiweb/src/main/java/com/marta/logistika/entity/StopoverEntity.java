package com.marta.logistika.entity;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stopovers")
public class StopoverEntity extends AbstractEntity implements Comparable<StopoverEntity> {

    @Column(nullable = false)
    private int sequenceNo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city")
    private CityEntity city;

    //todo
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "stopover", nullable = false)
    private List<TransactionUnloadEntity> unloads = new ArrayList<>();

    //todo
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "stopover", nullable = false)
    private List<TransactionLoadEntity> loads = new ArrayList<>();

    @Column(name = "weight")
    private int totalWeight;

    @Column(name = "duration")
    private Duration estimatedDuration;

    public StopoverEntity() {
    }

    public StopoverEntity(CityEntity city, int sequenceNo) {
        this.city = city;
        this.sequenceNo = sequenceNo;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public List<TransactionUnloadEntity> getUnloads() {
        return unloads;
    }

    public void setUnloads(List<TransactionUnloadEntity> unloads) {
        this.unloads = unloads;
    }

    public List<TransactionLoadEntity> getLoads() {
        return loads;
    }

    public void setLoads(List<TransactionLoadEntity> loads) {
        this.loads = loads;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Duration estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    @Override
    public int compareTo(StopoverEntity o) {
        return Integer.compare(sequenceNo, o.sequenceNo);
    }

    public void addLoadFor(OrderEntity order) {
        loads.add(new TransactionLoadEntity(order));
    }

    public void addUnloadFor(OrderEntity order) {
        unloads.add(new TransactionUnloadEntity(order));
    }

    public int getIncrementalWeight() {
        return loads.stream().map(TransactionEntity::getOrder)
                .mapToInt(OrderEntity::getWeight).sum()
                - unloads.stream().map(TransactionEntity::getOrder)
                .mapToInt(OrderEntity::getWeight).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopoverEntity that = (StopoverEntity) o;
        return id == that.id && this.getSequenceNo() == that.getSequenceNo() && this.getCity() == that.getCity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StopoverEntity{" +
                "sequenceNo=" + sequenceNo +
                ", city=" + city.getName() +
                '}';
    }
}