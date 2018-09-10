package com.marta.logistika.exception;

public class SuchEntityAlreadyExistsException extends RuntimeException {

    public SuchEntityAlreadyExistsException(long id, Class aClass) {
        super(String.format("Entity %s with id %d already exists", aClass.getCanonicalName(), id));
    }

}
