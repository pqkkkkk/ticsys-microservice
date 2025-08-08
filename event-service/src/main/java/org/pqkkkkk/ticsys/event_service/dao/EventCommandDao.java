package org.pqkkkkk.ticsys.event_service.dao;

import org.pqkkkkk.ticsys.event_service.entity.Event;

public interface EventCommandDao {
    public Event createEvent(Event event);
    public Event updateEvent(Event event);
}
