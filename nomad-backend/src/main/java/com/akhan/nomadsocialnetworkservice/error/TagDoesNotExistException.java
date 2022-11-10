package com.akhan.nomadsocialnetworkservice.error;

public class TagDoesNotExistException extends RuntimeException {

    // private static final long serialVersionUID = ;

    public TagDoesNotExistException() {
        super();
    }

    public TagDoesNotExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TagDoesNotExistException(final String message) {
        super(message);
    }

    public TagDoesNotExistException(final Throwable cause) {
        super(cause);
    }

}