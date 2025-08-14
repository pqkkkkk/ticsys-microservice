package org.pqkkkkk.ticsys.event_service.dao;

import org.pqkkkkk.ticsys.event_service.dto.FilterObject.EventFillter;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventQueryDao {
    public Event findEventById(Long eventId);
    public Page<Event> findEvents(EventFillter eventFillter, Pageable pageable);
}
