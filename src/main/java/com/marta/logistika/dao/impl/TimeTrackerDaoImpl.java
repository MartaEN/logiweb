package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.TimeTrackerDao;
import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.entity.TimeTrackerEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

@Repository("timeRepository")
public class TimeTrackerDaoImpl extends AbstractDao<TimeTrackerEntity> implements TimeTrackerDao {

    @Override
    public long
    calculateMonthlyMinutes(DriverEntity driver, YearMonth month) {

        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime finish = month.atEndOfMonth().atTime(LocalTime.MAX);

        Long result = em.createQuery("SELECT SUM(t.minutes) FROM TimeTrackerEntity t " +
                "WHERE t.driver=:driver AND t.finish >=:start AND t.finish <=:finish ",
                Long.class)
                .setParameter("driver", driver)
                .setParameter("start", start)
                .setParameter("finish", finish)
                .getSingleResult();

        return result == null ? 0 : result;
    }
}
