package com.akhan.nomadsocialnetworkservice.error;

public class FieldDoesNotExistException extends RuntimeException {

    // private static final long serialVersionUID = ;

    public FieldDoesNotExistException() {
        super();
    }

    public FieldDoesNotExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FieldDoesNotExistException(final String message) {
        super(message);
    }

    public FieldDoesNotExistException(final Throwable cause) {
        super(cause);
    }

}
