package com.akhan.nomadsocialnetworkservice.error;

public final class EntityNotFoundException extends RuntimeException {

    // private static final long serialVersionUID = ;

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }

    public EntityNotFoundException(final Throwable cause) {
        super(cause);
    }

}
