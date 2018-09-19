package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.DriverDao;
import com.marta.logistika.dto.DriverRecord;
import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.service.ServiceException;
import com.marta.logistika.service.api.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service("driverService")
public class DriverServiceImpl extends AbstractService implements DriverService {

    private final DriverDao driverDao;

    @Autowired
    public DriverServiceImpl(DriverDao driverDao) {
        this.driverDao = driverDao;
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
    public List<DriverRecord> listAll() {
        return driverDao.listAll().stream()
                .map(d -> mapper.map(d, DriverRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public DriverRecord findDriverByPersonalId(String personalId) {
        return mapper.map(driverDao.findByPersonalId(personalId), DriverRecord.class);
    }

    private boolean isDriverRecordValid (DriverRecord driverRecord) {
        if ( ! driverRecord.getPersonalId().matches("^[0-9]{6}$")) return false;
        if ( ! driverRecord.getFirstName().matches("^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$")) return false;
        if ( ! driverRecord.getLastName().matches("^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$")) return false;
        return true;
    }

}
