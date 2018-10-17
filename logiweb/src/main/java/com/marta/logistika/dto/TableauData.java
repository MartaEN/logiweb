package com.marta.logistika.dto;

import java.io.Serializable;
import java.util.List;

public class TableauData implements Serializable {

    private List<OrderRecordShort> orders;

    public List<OrderRecordShort> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderRecordShort> orders) {
        this.orders = orders;
    }
}
