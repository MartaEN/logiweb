package com.marta.logistika.exception.unchecked;

public class EntityNotFoundException extends UncheckedServiceException {

    public EntityNotFoundException(long id, Class aClass) {
        super(String.format("Entity %s with id %d not found", aClass.getCanonicalName(), id));
    }

}
