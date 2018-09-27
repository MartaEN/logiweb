package com.marta.logistika.dao.api;

import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.entity.TimeTrackerEntity;

import java.time.YearMonth;

public interface TimeTrackerDao extends GenericDao<TimeTrackerEntity> {

    boolean hasOpenTimeRecord(DriverEntity driver);
    TimeTrackerEntity getOpenTimeRecord(DriverEntity driver);
    long calculateMonthlyMinutes (DriverEntity driver, YearMonth month);

}
