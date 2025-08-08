package org.pqkkkkk.ticsys.event_service.exception;

public class EventNotExistException extends RuntimeException {
    public EventNotExistException(Long eventId) {
        super("Event with ID " + eventId + " does not exist.");
    }

    public EventNotExistException(String message) {
        super(message);
    }

}
