package com.akhan.nomadsocialnetworkservice.error;

public final class TagAlreadyExistWithinEventException extends RuntimeException {

    // private static final long serialVersionUID = ;

    public TagAlreadyExistWithinEventException() {
        super();
    }

    public TagAlreadyExistWithinEventException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TagAlreadyExistWithinEventException(final String message) {
        super(message);
    }

    public TagAlreadyExistWithinEventException(final Throwable cause) {
        super(cause);
    }

}
