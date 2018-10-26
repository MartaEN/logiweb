package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.TruckDao;
import com.marta.logistika.dto.TruckFilterForm;
import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.entity.TruckEntity;
import com.marta.logistika.event.EntityUpdateEvent;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.service.api.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("truckService")
public class TruckServiceImpl extends AbstractService implements TruckService {

    private final TruckDao truckDao;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public TruckServiceImpl(TruckDao truckDao, ApplicationEventPublisher applicationEventPublisher) {
        this.truckDao = truckDao;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void add(TruckRecord truck) {
        if (truckDao.regNumberExists(truck.getRegNumber())) {
            throw new ServiceException(String.format("Truck with registration number %s already exists", truck.getRegNumber()));
        } else if (isTruckRecordValid(truck)) {
            truck.setParked(true);
            truckDao.add(mapper.map(truck, TruckEntity.class));
            applicationEventPublisher.publishEvent(new EntityUpdateEvent());
        } else {
            throw new ServiceException("Truck data invalid");
        }
    }

    @Override
    @Transactional
    public void update(TruckRecord truckEditFormInput) {
        try {
            TruckEntity truckEntity = truckDao.findByRegNumber(truckEditFormInput.getRegNumber());
            if (isTruckRecordValid(truckEditFormInput)) {
                truckEntity.setCapacity(truckEditFormInput.getCapacity());
                truckEntity.setShiftSize(truckEditFormInput.getShiftSize());
                truckEntity.setServiceable(truckEditFormInput.isServiceable());
                applicationEventPublisher.publishEvent(new EntityUpdateEvent());
            } else {
                throw new ServiceException("Truck data invalid");
            }
        } catch (NoResultException e) {
            throw new ServiceException(String.format("Truck with registration number %s does not exist", truckEditFormInput.getRegNumber()));
        }
    }

    @Override
    @Transactional
    public void remove(TruckRecord truck) {
        truckDao.remove(truckDao.findByRegNumber(truck.getRegNumber()));
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
    }

    @Override
    public List<TruckRecord> listAll() {
        return truckDao.listAll()
                .stream()
                .map(truck -> mapper.map(truck, TruckRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TruckRecord> listAllFilteredBy(TruckFilterForm filter) {
        return truckDao.listAllFilteredBy(filter)
                .stream()
                .map(truck -> mapper.map(truck, TruckRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public TruckRecord findTruckByRegNum(String regNum) {
        return mapper.map(truckDao.findByRegNumber(regNum), TruckRecord.class);
    }

    /**
     * Prepares summary statistics on total number of online / offline / unserviceable trucks
     *
     * @return map with resulting statistics
     */
    @Override
    public LinkedHashMap<String, Long> getTruckStatistics() {
        LinkedHashMap<String, Long> briefTruckStats = new LinkedHashMap<>();
        briefTruckStats.put("ONLINE", truckDao.countOnline());
        briefTruckStats.put("OFFLINE", truckDao.countOffline());
        briefTruckStats.put("UNSERVICEABLE", truckDao.countUnserviceable());
        return briefTruckStats;
    }

    private boolean isTruckRecordValid(TruckRecord truckRecord) {
        if (!truckRecord.getRegNumber().matches("^[a-zA-Z]{2}[0-9]{5}$")) return false;
        if (truckRecord.getCapacity() < 0) return false;
        if (truckRecord.getShiftSize() < 0 || truckRecord.getShiftSize() > 2) return false;
        return true;
    }
}
