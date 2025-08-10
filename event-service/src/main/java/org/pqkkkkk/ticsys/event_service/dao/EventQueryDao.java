package org.pqkkkkk.ticsys.event_service.dao;

import org.pqkkkkk.ticsys.event_service.entity.Event;

public interface EventQueryDao {
    public Event findEventById(Long eventId);
}
