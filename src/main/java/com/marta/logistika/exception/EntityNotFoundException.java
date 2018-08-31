package com.marta.logistika.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(long id, Class aClass) {
        super("Entity " + aClass.getCanonicalName() + " not found with id " + id);
    }

}
