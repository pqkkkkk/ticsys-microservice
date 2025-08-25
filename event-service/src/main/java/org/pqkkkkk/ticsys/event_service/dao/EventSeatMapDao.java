package org.pqkkkkk.ticsys.event_service.dao;

import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;

public interface EventSeatMapDao {
    public EventSeatMap createEventSeatMap(EventSeatMap eventSeatMap);
    public EventSeatMap updateEventSeatMap(EventSeatMap eventSeatMap);
}
