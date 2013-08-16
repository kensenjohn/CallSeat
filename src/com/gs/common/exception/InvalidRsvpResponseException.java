package com.gs.common.exception;

import com.gs.common.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/16/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidRsvpResponseException extends Exception {
    private static final long serialVersionUID = -5588421068077476571L;
    private String message = "";
    public InvalidRsvpResponseException() {
        super();
        this.message = "An error occurred while trying to RSVP for an event.";
    }

    public InvalidRsvpResponseException(String message) {
        super(message);
        this.message = message;
    }

    public InvalidRsvpResponseException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
