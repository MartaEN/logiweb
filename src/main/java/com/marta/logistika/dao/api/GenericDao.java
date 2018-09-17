package com.marta.logistika.dao.api;

import com.marta.logistika.entity.AbstractEntity;

public interface GenericDao<T extends AbstractEntity> {

    void add(T entity);
    void merge(T entity);
    void remove(T entity);

}
