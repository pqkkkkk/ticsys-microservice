package org.pqkkkkk.ticsys.event_service.service.impl;

import java.util.ArrayList;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.dao.EventQueryDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.exception.NotEnoughInfoException;
import org.pqkkkkk.ticsys.event_service.service.EventCreationService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class SettingCreationService implements EventCreationService {
    private final EventCommandDao eventCommandDao;
    private final EventQueryDao eventQueryDao;

    public SettingCreationService(EventCommandDao eventDao, EventQueryDao eventQueryDao) {
        this.eventCommandDao = eventDao;
        this.eventQueryDao = eventQueryDao;
    }
    @Override
    public boolean canHandle(Event event) {
        if(event.getEventId() == null)
            throw new NotEnoughInfoException("Event ID is required to proceed with event creation.");
        if(event.getEventVisibility() == null)
            throw new NotEnoughInfoException("Event visibility information is required to proceed with event creation.");
        
        return true;
    }

    @Override
    @Transactional
    public Event process(Event inputEvent, Integer currentStep) {
        canHandle(inputEvent);

        Event existingEvent = eventQueryDao.findEventById(inputEvent.getEventId());
        if(existingEvent == null) {
            throw new EventNotExistException("Event does not exist.");
        }

        existingEvent.setEventVisibility(inputEvent.getEventVisibility());
        existingEvent = modifyEventFAQs(existingEvent, inputEvent);

        if(isCompleted(currentStep))
            existingEvent.setEventStatus(EventStatus.PENDING);

        existingEvent = eventCommandDao.updateEvent(existingEvent);

        if(existingEvent == null){
            throw new EventNotExistException(inputEvent.getEventId());
        }

        return existingEvent;
    }
    private Event modifyEventFAQs(Event existingEvent, Event inputEvent){
        if(existingEvent.getEventFAQs() == null) {
            existingEvent.setEventFAQs(new ArrayList<>());
        }
        existingEvent.getEventFAQs().addAll(inputEvent.getEventFAQs());

        return existingEvent;
    }
    @Override
    public EventCreationStep getStep() {
        return EventCreationStep.SETTINGS;
    }

}
