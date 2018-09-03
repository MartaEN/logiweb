package com.marta.logistika.exception;

public class NoReturnRoadException extends RuntimeException {

    public NoReturnRoadException(long id) {
        super("Return road for RoadEntity with id " + id + "not found in persistence context");
    }

}
