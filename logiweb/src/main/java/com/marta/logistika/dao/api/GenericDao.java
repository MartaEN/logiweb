package com.marta.logistika.dao.api;

import com.marta.logistika.entity.AbstractEntity;

public interface GenericDao<T extends AbstractEntity> {

    long add(T entity);
    void merge(T entity);
    void remove(T entity);

}
