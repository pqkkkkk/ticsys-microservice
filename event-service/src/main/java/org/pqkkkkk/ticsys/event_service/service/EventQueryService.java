package org.pqkkkkk.ticsys.event_service.service;

import org.pqkkkkk.ticsys.event_service.dto.filter_object.EventFilter;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.data.domain.Page;

public interface EventQueryService {
    public Event getEventById(Long eventId);
    public Page<Event> getEvents(EventFilter eventFillter);
    public boolean isValidEvent(Long eventId);
    public boolean isValidEventDate(Long eventId, Long eventDateId);
}
    