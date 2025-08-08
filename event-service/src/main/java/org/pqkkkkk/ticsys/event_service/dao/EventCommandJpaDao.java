package org.pqkkkkk.ticsys.event_service.dao;

import org.pqkkkkk.ticsys.event_service.dao.jpa_repository.EventRepository;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.stereotype.Repository;

@Repository
public class EventCommandJpaDao implements EventCommandDao {

    private final EventRepository eventRepository;

    public EventCommandJpaDao(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        if(event == null || !eventRepository.existsById(event.getEventId())) {
            return null; // or throw an exception
        }

        return eventRepository.save(event);
    }

}
