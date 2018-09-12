package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.TruckDao;
import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.entity.TruckEntity;
import com.marta.logistika.service.api.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("truckService")
public class TruckServiceImpl extends AbstractService implements TruckService {

    private final TruckDao truckDao;

    @Autowired
    public TruckServiceImpl(TruckDao truckDao) {
        this.truckDao = truckDao;
    }

    @Override
    @Transactional
    public void add(TruckRecord truck) {
        truckDao.add(mapper.map(truck, TruckEntity.class));
    }

    @Override
    @Transactional
    public void update(TruckRecord truckEditFormInput) {
        TruckEntity truckEntity = truckDao.findByRegNumber(truckEditFormInput.getRegNumber());
        truckEntity.setCapacity(truckEditFormInput.getCapacity());
        truckEntity.setShiftSize(truckEditFormInput.getShiftSize());
        truckEntity.setServiceable(truckEditFormInput.isServiceable());
    }

    @Override
    @Transactional
    public void remove(TruckRecord truck) {
        truckDao.remove(truckDao.findByRegNumber(truck.getRegNumber()));
    }

    @Override
    public List<TruckRecord> listAll() {
        return truckDao.listAll()
                .stream()
                .map(truck -> mapper.map(truck, TruckRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public TruckRecord getTruckByRegNum(String regNum) {
        return mapper.map(truckDao.findByRegNumber(regNum), TruckRecord.class);
    }
}
