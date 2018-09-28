package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.DriverDao;
import com.marta.logistika.dao.api.TripTicketDao;
import com.marta.logistika.dto.DriverRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.service.api.DriverService;
import com.marta.logistika.service.api.TimeTrackerService;
import com.marta.logistika.service.api.TripTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("driverService")
public class DriverServiceImpl extends AbstractService implements DriverService {

    private final static int MIN_REST_INTERVAL_IN_HOURS = 12;
    private final static int MAX_MONHTLY_WORK_LIMIT_IN_MINUTES = 176 * 60;

    private final DriverDao driverDao;
    private final TripTicketDao ticketDao;
    private final TripTicketService ticketService;
    private final TimeTrackerService timeService;

    @Autowired
    public DriverServiceImpl(DriverDao driverDao, TripTicketDao ticketDao, TripTicketService ticketService, TimeTrackerService timeService) {
        this.driverDao = driverDao;
        this.ticketDao = ticketDao;
        this.ticketService = ticketService;
        this.timeService = timeService;
    }

    @Override
    @Transactional
    public void add(DriverRecord driver) {
        if(driverDao.personalIdExists(driver.getPersonalId())) {
            throw new ServiceException(String.format("Employee with personal id %s already exists", driver.getPersonalId()));
        }
        System.out.println(mapper.map(driver, DriverEntity.class));
        driverDao.add(mapper.map(driver, DriverEntity.class));
    }

    @Override
    @Transactional
    public void update(DriverRecord driverEditFormInput) {
        try {
            DriverEntity driverEntity = driverDao.findByPersonalId(driverEditFormInput.getPersonalId());
            if(isDriverRecordValid(driverEditFormInput)) {
                driverEntity.setFirstName(driverEditFormInput.getFirstName());
                driverEntity.setLastName(driverEditFormInput.getLastName());
                driverEntity.setLocation(driverEditFormInput.getLocation());
            } else {
                throw new ServiceException("Driver data invalid");
            }
        } catch (NoResultException e) {
            throw new ServiceException(String.format("Driver with personal id %s does not exist", driverEditFormInput.getPersonalId()));
        }
    }

    @Override
    @Transactional
    public void remove(String personalId) {
        driverDao.remove(driverDao.findByPersonalId(personalId));
    }

    @Override
    public boolean personalIdExists(String personalId) {
        return driverDao.personalIdExists(personalId);
    }

    @Override
    public boolean usernameExists(String username) {
        return driverDao.usernameExists(username);
    }

    @Override
    public DriverRecord findDriverByPersonalId(String personalId) {
        return mapper.map(driverDao.findByPersonalId(personalId), DriverRecord.class);
    }

    @Override
    public String findPersonalIdByUsername(String username) {
        return driverDao.findByUsername(username).getPersonalId();
    }

    @Override
    public List<DriverRecord> listAll() {
        return driverDao.listAll().stream()
                .map(d -> mapper.map(d, DriverRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverRecord> findDrivers(long ticketId) {

        TripTicketEntity ticket = ticketDao.findById(ticketId);
        CityEntity fromCity = ticket.getStopoverWithSequenceNo(0).getCity();
        LocalDateTime departureDateTime = ticket.getDepartureDateTime();

        List<DriverEntity> driversList = driverDao.listAllAvailable(fromCity, departureDateTime.minusHours(MIN_REST_INTERVAL_IN_HOURS));

        Map<YearMonth, Long> plannedTripTimeInMinutes = ticketService.getPlannedMinutesByYearMonth(ticket);

        plannedTripTimeInMinutes.keySet().forEach(month -> {
            driversList.removeIf(driver ->
                    timeService.calculateMonthlyMinutes(driver, month) + plannedTripTimeInMinutes.get(month)
                            > MAX_MONHTLY_WORK_LIMIT_IN_MINUTES);
        });

        return driversList.stream()
                .map(d -> mapper.map(d, DriverRecord.class))
                .collect(Collectors.toList());
    }


    private boolean isDriverRecordValid (DriverRecord driverRecord) {
        if ( ! driverRecord.getPersonalId().matches("^[0-9]{6}$")) return false;
        if ( ! driverRecord.getFirstName().matches("^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$")) return false;
        if ( ! driverRecord.getLastName().matches("^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$")) return false;
        return true;
    }

}
