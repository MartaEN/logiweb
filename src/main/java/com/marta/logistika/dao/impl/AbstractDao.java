package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.GenericDao;
import com.marta.logistika.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

abstract class AbstractDao<T extends AbstractEntity> implements GenericDao<T> {

    @PersistenceContext
    EntityManager em;

    @Override
    public void add(T entity) {
        em.persist(entity);
    }

    @Override
    public void merge(T entity) {
        em.merge(entity);
    }

    @Override
    public void remove(T entity) {
        em.remove(entity);
    }



}
