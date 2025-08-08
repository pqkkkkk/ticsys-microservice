package org.pqkkkkk.ticsys.event_service.service.impl;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.exception.NotEnoughInfoException;
import org.pqkkkkk.ticsys.event_service.service.EventCreationService;
import org.springframework.stereotype.Service;

@Service
public class TimeAndTicketCreationService implements EventCreationService {
    private EventCommandDao eventCommandDao;

    public TimeAndTicketCreationService(EventCommandDao eventDao) {
        this.eventCommandDao = eventDao;
    }
    @Override
    public boolean canHandle(Event event) {
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
    public Event process(Event event, Integer currentStep) {
        canHandle(event);

        if(isCompleted(currentStep))
            event.setEventStatus(EventStatus.PENDING);
        

        event =  eventCommandDao.updateEvent(event);

        if(event == null){
            throw new EventNotExistException("Event not found");
        }

        return event;
    }
    @Override
    public EventCreationStep getStep() {
        return EventCreationStep.TIME_AND_TICKET;
    }

}
