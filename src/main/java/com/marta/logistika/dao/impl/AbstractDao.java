package com.marta.logistika.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

abstract class AbstractDao {

    @PersistenceContext
    EntityManager em;

}
