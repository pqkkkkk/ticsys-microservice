package org.pqkkkkk.ticsys.event_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.event_service.Contants.EventSeatMapZoneShape;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMapZone;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public class EventSeatMapServiceTest {
    @Autowired
    private EventSeatMapService eventSeatMapService;
    @MockitoBean
    private EventQueryService eventQueryService;
    @Autowired
    EventCommandDao eventCommandDao;

    @Test
    public void createEventSeatMap_WithInvalidEvent_ShowThrowException(){
        // Given
        EventDate eventDate = EventDate.builder()
                .date(LocalDate.now())
                .event(Event.builder().eventId(1L).build())
                .build();
        EventSeatMap eventSeatMap = EventSeatMap.builder()
                .eventDate(eventDate)
                .build();
        // When
        when(eventQueryService.isValidEvent(eventSeatMap.getEventDate().getEvent().getEventId())).thenReturn(false);
        // Then
        assertThrows(EventNotExistException.class, () -> eventSeatMapService.createEventSeatMap(eventSeatMap));
    }
    @Test
    public void createEventSeatMap_WithValidData_ShouldCreateSeatMap(){
        // Given
        Event event = Event.builder()
            .eventName("Event 1")
            .userId(1L)
            .build();

        EventDate eventDate = EventDate.builder()
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(2))
                .event(event)
                .build();
        event.setEventDates(List.of(eventDate));
        
        Ticket ticket = Ticket.builder()
            .eventDate(eventDate)
            .ticketPrice(100000.0)
            .ticketName("Standard Ticket")
            .ticketTotalQuantity(500)
            .ticketStockQuantity(500)
            .build();
        
        eventDate.setTickets(List.of(ticket));
        eventCommandDao.createEvent(event);

        EventSeatMap eventSeatMap = EventSeatMap.builder()
                .eventDate(eventDate)
                .build();
        
        EventSeatMapZone zone1 = EventSeatMapZone.builder()
                .zoneName("Zone 1")
                .zoneShape(EventSeatMapZoneShape.RECTANGLE)
                .eventSeatMap(eventSeatMap)
                .fill("#FF5733")
                .ticket(ticket)
                .ticketQuantity(100)
                .build();
        EventSeatMapZone zone2 = EventSeatMapZone.builder()
                .zoneName("Zone 2")
                .zoneShape(EventSeatMapZoneShape.RECTANGLE)
                .eventSeatMap(eventSeatMap)
                .fill("#33FF57")
                .ticket(ticket)
                .ticketQuantity(150)
                .build();
        
        eventSeatMap.setEventSeatMapZones(List.of(zone1, zone2));

        // When
        when(eventQueryService.isValidEvent(eventSeatMap.getEventDate().getEvent().getEventId())).thenReturn(true);
        EventSeatMap createdEventSeatMap = eventSeatMapService.createEventSeatMap(eventSeatMap);

        // Then
        assertNotNull(createdEventSeatMap);
        assertEquals(2, createdEventSeatMap.getEventSeatMapZones().size());
    }

    @Test
    public void updateEventSeatMap_WithNullId_ShouldThrowException(){
        // Given
        EventSeatMap eventSeatMap = EventSeatMap.builder()
                .eventDate(EventDate.builder().event(Event.builder().eventId(1L).build()).build())
                .build();
        // When
        when(eventQueryService.isValidEvent(eventSeatMap.getEventDate().getEvent().getEventId())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class, () -> eventSeatMapService.updateEventSeatMap(eventSeatMap));
    }
}
