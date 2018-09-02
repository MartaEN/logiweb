package com.marta.logistika.service.impl;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public abstract class AbstractService {

    Mapper mapper = new DozerBeanMapper();

}
