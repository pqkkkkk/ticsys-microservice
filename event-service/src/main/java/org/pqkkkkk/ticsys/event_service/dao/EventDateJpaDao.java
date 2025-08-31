package org.pqkkkkk.ticsys.event_service.dao;

import org.pqkkkkk.ticsys.event_service.dao.jpa_repository.EventDateRepository;
import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.springframework.stereotype.Repository;

@Repository
public class EventDateJpaDao implements EventDateDao {

    private final EventDateRepository eventDateRepository;

    public EventDateJpaDao(EventDateRepository eventDateRepository) {
        this.eventDateRepository = eventDateRepository;
    }

    @Override
    public EventDate findEventDateById(Long eventDateId) {
        return eventDateRepository.findById(eventDateId).orElse(null);
    }

}
