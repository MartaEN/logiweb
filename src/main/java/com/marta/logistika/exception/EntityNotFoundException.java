package com.marta.logistika.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(long id, Class aClass) {
        super(String.format("Entity %s with id %d not found", aClass.getCanonicalName(), id));
    }

}
