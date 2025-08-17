package org.pqkkkkk.ticsys.event_service.service.impl;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.dao.EventQueryDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.service.EventUpdationService;
import org.springframework.stereotype.Service;

@Service
public class BasicInfoUpdationService extends BasicInfoCreationService implements EventUpdationService {
    private final EventCommandDao eventCommandDao;
    private final EventQueryDao eventQueryDao;

    public BasicInfoUpdationService(EventCommandDao eventCommandDao, EventQueryDao eventQueryDao) {
        super(eventCommandDao);
        this.eventCommandDao = eventCommandDao;
        this.eventQueryDao = eventQueryDao;
    }
    @Override
    public boolean canHandle(Event event) {
        return super.canHandle(event);
    }

    @Override
    public Event process(Event event, Integer currentStep) {
        Event existingEvent = eventQueryDao.findEventById(event.getEventId());

        if(existingEvent == null){
            throw new EventNotExistException(event.getEventId());
        }
        existingEvent = setNewEventValues(existingEvent, event);

        eventCommandDao.updateEvent(existingEvent);
        return existingEvent;
    }

    private Event setNewEventValues(Event existingEvent, Event inputEvent) {
        // Add additional logic
        return existingEvent;
    }
    @Override
    public EventCreationStep getStep() {
        return EventCreationStep.BASIC_INFO;
    }
}
