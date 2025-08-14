package org.pqkkkkk.ticsys.event_service.service;

import org.pqkkkkk.ticsys.event_service.dto.FilterObject.EventFillter;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.data.domain.Page;

public interface EventQueryService {
    public Event getEventById(Long eventId);
    public Page<Event> getEvents(EventFillter eventFillter);
}
