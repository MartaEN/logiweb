package com.marta.logistika.exception.unchecked;

public class UncheckedServiceException extends RuntimeException {

    public UncheckedServiceException(String message) {
        super(message);
    }

    public UncheckedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UncheckedServiceException(Throwable cause) {
        super(cause);
    }

}
