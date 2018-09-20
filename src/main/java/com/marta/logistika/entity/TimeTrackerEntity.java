package com.marta.logistika.entity;

import com.marta.logistika.enums.DriverStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "time_tracker")
public class TimeTrackerEntity extends AbstractEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver")
    private DriverEntity driver;

    @Column(name="start")
    private LocalDateTime start;

    @Column(name="finish")
    private LocalDateTime finish;

    @Column(name="minutes")
    private long minutes;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    public DriverEntity getDriver() {
        return driver;
    }

    public void setDriver(DriverEntity driver) {
        this.driver = driver;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTrackerEntity that = (TimeTrackerEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
