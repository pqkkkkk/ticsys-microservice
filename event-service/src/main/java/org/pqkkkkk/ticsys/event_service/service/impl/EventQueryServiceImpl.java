package org.pqkkkkk.ticsys.event_service.service.impl;

import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.dao.EventQueryDao;
import org.pqkkkkk.ticsys.event_service.dto.filter_object.EventFilter;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.service.EventQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EventQueryServiceImpl implements EventQueryService {
    private final EventQueryDao eventQueryDao;

    public EventQueryServiceImpl(EventQueryDao eventQueryDao) {
        this.eventQueryDao = eventQueryDao;
    }

    @Override
    public Event getEventById(Long eventId) {
        Event event = eventQueryDao.findEventById(eventId);

        return event;
    }

    @Override
    public Page<Event> getEvents(EventFilter eventFillter) {
        Sort sort = Sort.by(eventFillter.getSortDirection(), eventFillter.getSortBy());

        Pageable pageable = PageRequest.of(eventFillter.getCurrentPage() - 1, eventFillter.getPageSize(),
                         sort);

        Page<Event> events = eventQueryDao.findEvents(eventFillter, pageable);

        return events;
    }

    @Override
    public boolean isValidEvent(Long eventId) {
        if(eventId == null) {
            return false;
        }

        Event event = eventQueryDao.findEventById(eventId);

        if(event == null || event.getEventStatus() != EventStatus.ON_GOING){
            return false;
        }

        return true;
    }

}
