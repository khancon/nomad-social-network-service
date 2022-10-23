package com.akhan.nomadsocialnetworkservice.error;

public final class EventNotFoundException extends RuntimeException {

    // private static final long serialVersionUID = ;

    public EventNotFoundException() {
        super();
    }

    public EventNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EventNotFoundException(final String message) {
        super(message);
    }

    public EventNotFoundException(final Throwable cause) {
        super(cause);
    }

}
