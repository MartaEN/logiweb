package com.marta.logistika.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

abstract class AbstractDao {

    EntityManager em = Persistence.createEntityManagerFactory("logistika-persistence-unit").createEntityManager();

}
