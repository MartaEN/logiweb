package com.marta.logistika.service.impl;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.time.LocalDateTime;

public abstract class AbstractService {

    Mapper mapper = new DozerBeanMapper();
    final static LocalDateTime MAX_FUTURE_DATE = LocalDateTime.of(2099, 12, 31, 23, 59);

}
