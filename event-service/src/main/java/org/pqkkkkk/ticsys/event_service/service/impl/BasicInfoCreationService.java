package org.pqkkkkk.ticsys.event_service.service.impl;


import java.util.List;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.Contants.EventLocationType;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.Contants.UserRoleInEvent;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.pqkkkkk.ticsys.event_service.exception.NotEnoughInfoException;
import org.pqkkkkk.ticsys.event_service.service.EventCreationService;
import org.springframework.stereotype.Service;

@Service
public class BasicInfoCreationService implements EventCreationService {
    private final EventCommandDao eventCommandDao;

    public BasicInfoCreationService(EventCommandDao eventDao){
        this.eventCommandDao = eventDao;
    }
    @Override
    public boolean canHandle(Event event) {
        if(event.getEventName() == null || event.getEventName().isBlank())
            throw new NotEnoughInfoException("Event name is required to proceed with event creation.");

        if(event.getEventDescription() == null || event.getEventDescription().isBlank())
            throw new NotEnoughInfoException("Event description is required to proceed with event creation.");
            
        if(event.getEventOrganizer() == null)
            throw new NotEnoughInfoException("Event organizer information is required to proceed with event creation.");
        
        if(event.getEventLocationType() == EventLocationType.OFFLINE && event.getEventOfflineLocation() == null)
            throw new NotEnoughInfoException("Offline location information is required for offline events.");
        
        if(event.getEventLocationType() == EventLocationType.ONLINE && event.getEventOnlineLocation() == null)
            throw new NotEnoughInfoException("Online location information is required for online events.");
            
        if(event.getEventOfflineLocation() != null && event.getEventOnlineLocation() != null)
            throw new NotEnoughInfoException("Both offline and online location information cannot be provided.");

        return true;
    }
    private Event addDefaultAdmin(Event event) {
        EventMember admin = EventMember.builder()
                .userId(event.getUserId())
                .userRoleInEvent(UserRoleInEvent.ADMIN)
                .build();

        event.setEventMembers(List.of(admin));

        return event;
    }
    @Override
    public Event process(Event event, Integer currentStep) {
        canHandle(event);

        addDefaultAdmin(event);

        if(isCompleted(currentStep))
            event.setEventStatus(EventStatus.PENDING);

        return eventCommandDao.createEvent(event);
    }
    @Override
    public EventCreationStep getStep() {
        return EventCreationStep.BASIC_INFO;
    }


}
