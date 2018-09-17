package com.marta.logistika.dao.api;

import com.marta.logistika.entity.OrderEntity;

public interface OrderDao extends GenericDao<OrderEntity> {

    OrderEntity findById (long id);
}
