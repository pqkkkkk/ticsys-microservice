package org.pqkkkkk.ticsys.event_service.service.impl;

import java.util.ArrayList;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.dao.EventQueryDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.exception.NotEnoughInfoException;
import org.pqkkkkk.ticsys.event_service.service.EventCreationService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class TimeAndTicketCreationService implements EventCreationService {
    private EventCommandDao eventCommandDao;
    private EventQueryDao eventQueryDao;

    public TimeAndTicketCreationService(EventCommandDao eventDao, EventQueryDao eventQueryDao) {
        this.eventCommandDao = eventDao;
        this.eventQueryDao = eventQueryDao;
    }
    @Override
    public boolean canHandle(Event event) {
        if(event.getEventId() == null)
            throw new NotEnoughInfoException("Event ID is required to proceed with event creation.");
        if(event.getEventDates() == null || event.getEventDates().isEmpty()) {
            throw new NotEnoughInfoException("Event dates are required to proceed with event creation.");
        }
        for(EventDate date : event.getEventDates()) {
            if(date.getTickets() == null || date.getTickets().isEmpty()) {
                throw new NotEnoughInfoException("At least one ticket is required for each event date.");
            }
        }

        return true;
    }

    @Override
    @Transactional
    public Event process(Event event, Integer currentStep) {
        canHandle(event);

        Event existingEvent = eventQueryDao.findEventById(event.getEventId());
        if(existingEvent == null) {
            throw new EventNotExistException("Event does not exist.");
        }

        // Initialize and modify event dates
        if(existingEvent.getEventDates() == null) {
            existingEvent.setEventDates(new ArrayList<>());
        } else {
            existingEvent.getEventDates().clear(); // Clear existing dates
        }
        existingEvent.getEventDates().addAll(event.getEventDates());

        if(isCompleted(currentStep))
            existingEvent.setEventStatus(EventStatus.PENDING);
        
        existingEvent = eventCommandDao.updateEvent(existingEvent);

        if(existingEvent == null){
            throw new EventNotExistException("Event not found");
        }

        return existingEvent;
    }
    @Override
    public EventCreationStep getStep() {
        return EventCreationStep.TIME_AND_TICKET;
    }

}
