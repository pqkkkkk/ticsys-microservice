package org.pqkkkkk.ticsys.event_service.service.impl;

import org.pqkkkkk.ticsys.event_service.dao.EventSeatMapDao;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.service.EventQueryService;
import org.pqkkkkk.ticsys.event_service.service.EventSeatMapService;
import org.springframework.stereotype.Service;


@Service
public class EventSeatMapServiceImpl implements EventSeatMapService {
    private final EventSeatMapDao eventSeatMapDao;
    private final EventQueryService eventQueryService;

    public EventSeatMapServiceImpl(EventSeatMapDao eventSeatMapDao, EventQueryService eventQueryService) {
        this.eventSeatMapDao = eventSeatMapDao;
        this.eventQueryService = eventQueryService;
    }

    @Override
    public EventSeatMap createEventSeatMap(EventSeatMap eventSeatMap) {
        if(!eventQueryService.isValidEvent(eventSeatMap.getEventDate().getEvent().getEventId())){
            throw new EventNotExistException(eventSeatMap.getEventDate().getEvent().getEventId());
        }

        return eventSeatMapDao.createEventSeatMap(eventSeatMap);
    }

    @Override
    public EventSeatMap updateEventSeatMap(EventSeatMap eventSeatMap) {
        if(!eventQueryService.isValidEvent(eventSeatMap.getEventDate().getEvent().getEventId())){
            throw new EventNotExistException(eventSeatMap.getEventDate().getEvent().getEventId());
        }

        EventSeatMap updatedEventSeatMap = eventSeatMapDao.updateEventSeatMap(eventSeatMap);

        if(updatedEventSeatMap == null){
            throw new IllegalArgumentException("EventSeatMap with ID " + eventSeatMap.getEventSeatMapId() + " does not exist.");
        }
        
        return updatedEventSeatMap;
    }

}
