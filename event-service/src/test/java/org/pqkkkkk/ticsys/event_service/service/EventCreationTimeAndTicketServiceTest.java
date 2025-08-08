package org.pqkkkkk.ticsys.event_service.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.client.identity.IdentityClient;
import org.pqkkkkk.ticsys.event_service.dao.jpa_repository.EventRepository;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.exception.NotEnoughInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventCreationTimeAndTicketServiceTest {
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;
    @MockitoBean
    private IdentityClient identityClient;

    // --------------- Test cases for event creation time and ticket information -------------------------------
    @Test
    public void createEventStepTwo_WithNoEventDate_ShouldThrowException() {
        // Given
        Event event = new Event();
        event.setEventDates(null);

        // When
        when(identityClient.isValidUser(event.getUserId())).thenReturn(true);

        // Act & Assert
        assertThrows(NotEnoughInfoException.class, () -> {
            eventService.createEvent(event, EventCreationStep.TIME_AND_TICKET.getStep());
        });
    }
    @Test
    public void createEventStepTwo_WithNoTicket_ShouldThrowException() {
        // Given
        Event event = new Event();
        event.setEventDates(List.of(
            EventDate.builder()
                .date(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .tickets(null) // No tickets provided
                .build()
        ));

        // When
        when(identityClient.isValidUser(event.getUserId())).thenReturn(true);

        // Act & Assert
        assertThrows(NotEnoughInfoException.class, () -> {
            eventService.createEvent(event, EventCreationStep.TIME_AND_TICKET.getStep());
        });
    }
    @Test
    public void createEventStepTwo_WithNonExistentEvent_ShouldThrowException() {
        // Given
        Event event = new Event();
        event.setUserId(1L);
        
        EventDate eventDate = EventDate.builder()
            .date(LocalDate.now())
            .startTime(LocalTime.of(10, 0))
            .endTime(LocalTime.of(12, 0))
            .event(event)
            .build();
        
        event.setEventDates(List.of(eventDate));
        
        List<Ticket> tickets = List.of(
            Ticket.builder()
                .ticketName("Standard Ticket")
                .ticketPrice(100.0)
                .ticketService("Standard Service")
                .soldStartTime(LocalDateTime.now())
                .soldEndTime(LocalDateTime.now().plusHours(1))
                .eventDate(eventDate)
                .build()
        );
        eventDate.setTickets(tickets);

        // When
        when(identityClient.isValidUser(event.getUserId())).thenReturn(true);

        // Act & Assert
        assertThrows(EventNotExistException.class, () -> {
            eventService.createEvent(event, EventCreationStep.TIME_AND_TICKET.getStep());
        });
    }
    @Test
    public void createEventStepTwo_WithValidData_ShouldCreateEvent() {
        // Given
        Event event = Event.builder()
            .userId(1L)
            .eventName("Test Event")
            .eventDescription("This is a test event")
            .build();
        eventRepository.save(event); // Save the event to ensure it exists

        EventDate eventDate = EventDate.builder()
            .date(LocalDate.now())
            .startTime(LocalTime.of(10, 0))
            .endTime(LocalTime.of(12, 0))
            .event(event)
            .build();

        event.setEventDates(new ArrayList<>(List.of(eventDate)));

        List<Ticket> tickets = List.of(
            Ticket.builder()
                .ticketName("Standard Ticket")
                .ticketPrice(100.0)
                .ticketService("Standard Service")
                .ticketTotalQuantity(100)
                .ticketStockQuantity(100)
                .soldStartTime(LocalDateTime.now())
                .soldEndTime(LocalDateTime.now().plusHours(1))
                .eventDate(eventDate)
                .build()
        );
        eventDate.setTickets(new ArrayList<>(tickets));

        // When
        when(identityClient.isValidUser(event.getUserId())).thenReturn(true);

        // Act
        Event createdEvent = eventService.createEvent(event, EventCreationStep.TIME_AND_TICKET.getStep());

        // Assert
        assertNotNull(createdEvent);
        assertNotNull(createdEvent.getEventDates());
        assertFalse(createdEvent.getEventDates().isEmpty());
        assertNotNull(createdEvent.getEventDates().get(0).getTickets());
        assertFalse(createdEvent.getEventDates().get(0).getTickets().isEmpty());
    }
}
