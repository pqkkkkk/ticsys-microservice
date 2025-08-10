package org.pqkkkkk.ticsys.event_service.dao;

import org.pqkkkkk.ticsys.event_service.dao.jpa_repository.EventRepository;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.stereotype.Repository;

@Repository
public class EventQueryJpaDao implements EventQueryDao {
    private final EventRepository eventRepository;

    public EventQueryJpaDao(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    @Override
    public Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

}
