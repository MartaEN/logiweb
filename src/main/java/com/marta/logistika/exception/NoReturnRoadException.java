package com.marta.logistika.exception;

public class NoReturnRoadException extends RuntimeException {

    public NoReturnRoadException(long id) {
        super(String.format("Return road for RoadEntity with id %d not found in persistence context", id));
    }

}
