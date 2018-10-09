package com.marta.logistika.service.api;

import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.exception.ServiceException;

import java.time.YearMonth;

public interface TimeTrackerService {

    void openNewTimeRecord(DriverEntity driver) throws ServiceException;
    void closeReopenTimeRecord(DriverEntity driver);
    void closeTimeRecord(DriverEntity driver);
    long calculateMonthlyMinutes (DriverEntity driver, YearMonth month);

}
