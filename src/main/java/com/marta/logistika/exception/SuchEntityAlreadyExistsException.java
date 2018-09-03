package com.marta.logistika.exception;

public class SuchEntityAlreadyExistsException extends RuntimeException {

    public SuchEntityAlreadyExistsException(long id, Class aClass) {
        super("Entity " + aClass.getCanonicalName() + " already exists with id " + id);
    }

}
