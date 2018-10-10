package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.StopoverDao;
import com.marta.logistika.entity.StopoverEntity;
import org.springframework.stereotype.Repository;

@Repository("stopoverRepository")
public class StopoverDaoImpl extends AbstractDao<StopoverEntity> implements StopoverDao {
}
