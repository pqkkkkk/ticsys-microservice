package org.pqkkkkk.ticsys.event_service.service;

import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;

public interface EventSeatMapService {
    public EventSeatMap createEventSeatMap(EventSeatMap eventSeatMap);
    public EventSeatMap updateEventSeatMap(EventSeatMap eventSeatMap);
}
