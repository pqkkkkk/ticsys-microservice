package org.pqkkkkk.ticsys.event_service.dao;

import org.pqkkkkk.ticsys.event_service.dao.jpa_repository.EventSeatMapRepository;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;
import org.springframework.stereotype.Repository;

@Repository
public class EventSeatMapJpaDao implements EventSeatMapDao {
    private final EventSeatMapRepository eventSeatMapRepository;

    public EventSeatMapJpaDao(EventSeatMapRepository eventSeatMapRepository) {
        this.eventSeatMapRepository = eventSeatMapRepository;
    }

    @Override
    public EventSeatMap createEventSeatMap(EventSeatMap eventSeatMap) {
        if(eventSeatMap == null || eventSeatMap.getEventSeatMapId() != null) {
            return null; // Cannot create a new EventSeatMap with an existing ID
        }
        
        return eventSeatMapRepository.save(eventSeatMap);
    }

    @Override
    public EventSeatMap updateEventSeatMap(EventSeatMap eventSeatMap) {
        if(eventSeatMap == null || eventSeatMap.getEventSeatMapId() == null || !eventSeatMapRepository.existsById(eventSeatMap.getEventSeatMapId())) {
            return null;
        }

        return eventSeatMapRepository.save(eventSeatMap);
    }

}
