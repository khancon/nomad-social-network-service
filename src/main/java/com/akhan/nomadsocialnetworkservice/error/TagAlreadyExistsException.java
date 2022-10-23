package com.akhan.nomadsocialnetworkservice.error;

public final class TagAlreadyExistsException extends RuntimeException {

    // private static final long serialVersionUID = ;

    public TagAlreadyExistsException() {
        super();
    }

    public TagAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TagAlreadyExistsException(final String message) {
        super(message);
    }

    public TagAlreadyExistsException(final Throwable cause) {
        super(cause);
    }

}