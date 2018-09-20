package com.marta.logistika.service.api;

import com.marta.logistika.entity.DriverEntity;

import java.time.YearMonth;

public interface TimeTrackerService {

    long calculateMonthlyMinutes (DriverEntity driver, YearMonth month);
}
