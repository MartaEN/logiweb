package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.TimeTrackerDao;
import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.service.api.TimeTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service("timeService")
public class TimeTrackerServiceImpl extends AbstractService implements TimeTrackerService {

    private final TimeTrackerDao timeTrackerDao;

    @Autowired
    public TimeTrackerServiceImpl(TimeTrackerDao timeTrackerDao) {
        this.timeTrackerDao = timeTrackerDao;
    }

    @Override
    public long calculateMonthlyMinutes(DriverEntity driver, YearMonth month) {
        return timeTrackerDao.calculateMonthlyMinutes(driver, month);
    }
}
