package org.pqkkkkk.ticsys.event_service.service.impl;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.exception.NotEnoughInfoException;
import org.pqkkkkk.ticsys.event_service.service.EventCreationService;
import org.springframework.stereotype.Service;

@Service
public class SettingCreationService implements EventCreationService {
    private final EventCommandDao eventCommandDao;

    public SettingCreationService(EventCommandDao eventDao) {
        this.eventCommandDao = eventDao;
    }
    @Override
    public boolean canHandle(Event event) {
        if(event.getEventVisibility() == null)
            throw new NotEnoughInfoException("Event visibility information is required to proceed with event creation.");
        
        return true;
    }

    @Override
    public Event process(Event event, Integer currentStep) {
        canHandle(event);

        if(isCompleted(currentStep))
            event.setEventStatus(EventStatus.PENDING);

        return eventCommandDao.updateEvent(event);
    }
    @Override
    public EventCreationStep getStep() {
        return EventCreationStep.SETTINGS;
    }

}
