package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.TimeTrackerDao;
import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.entity.TimeTrackerEntity;
import com.marta.logistika.enums.DriverStatus;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.service.api.TimeTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service("timeService")
public class TimeTrackerServiceImpl extends AbstractService implements TimeTrackerService {

    private final TimeTrackerDao timeTrackerDao;

    @Autowired
    public TimeTrackerServiceImpl(TimeTrackerDao timeTrackerDao) {
        this.timeTrackerDao = timeTrackerDao;
    }

    /**
     * Method creates new time record entry for the driver
     * @param driver driver
     * @throws ServiceException in case previous time record was not closed
     */
    @Override
    @Transactional
    public void openNewTimeRecord(DriverEntity driver) throws ServiceException {
        if (timeTrackerDao.hasOpenTimeRecord(driver)) throw new ServiceException(String.format("Previous time record for driver %s is not closed", driver.toString()));
        TimeTrackerEntity timeRecord = new TimeTrackerEntity();
        timeRecord.setDriver(driver);
        timeRecord.setStart(LocalDateTime.now());
        timeTrackerDao.add(timeRecord);
    }

    @Override
    @Transactional
    public void closeReopenTimeRecord(DriverEntity driver, DriverStatus status) {
        closeTimeRecord(driver, status);
        TimeTrackerEntity timeRecord = new TimeTrackerEntity();
        timeRecord.setDriver(driver);
        timeRecord.setStart(LocalDateTime.now());
        timeTrackerDao.add(timeRecord);
    }

    /**
     * Method closes driver's time entry with a given status.
     * In case time record crosses midnight(s), is it split into separate records for each calendar day.
     * @param driver driver
     * @param status status to be used for closing the time record
     */
    @Override
    @Transactional
    public void closeTimeRecord(DriverEntity driver, DriverStatus status) {

        LocalDateTime timestamp = LocalDateTime.now();
        TimeTrackerEntity openRecord = timeTrackerDao.getOpenTimeRecord(driver);

        while (openRecord.getStart().isBefore(timestamp.toLocalDate().atStartOfDay())) {
            LocalDateTime divider = openRecord.getStart().plusDays(1).toLocalDate().atStartOfDay();
            openRecord.setStatus(status);
            openRecord.setFinish(divider.minusSeconds(1));
            openRecord.setMinutes(Duration.between(openRecord.getStart(), openRecord.getFinish()).toMinutes());
            timeTrackerDao.merge(openRecord);

            openRecord = new TimeTrackerEntity();
            openRecord.setDriver(driver);
            openRecord.setStart(divider);
            timeTrackerDao.add(openRecord);
        }

        openRecord.setStatus(status);
        openRecord.setFinish(timestamp);
        openRecord.setMinutes(Duration.between(openRecord.getStart(), openRecord.getFinish()).toMinutes());
        timeTrackerDao.merge(openRecord);
    }



    /**
     * Method returns time spent by the driver at work in the given month, in minutes
     * @param driver driver
     * @param month month and year
     * @return total time at work in minutes
     */
    @Override
    public long calculateMonthlyMinutes(DriverEntity driver, YearMonth month) {
        return timeTrackerDao.calculateMonthlyMinutes(driver, month);
    }
}
