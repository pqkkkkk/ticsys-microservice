package org.pqkkkkk.ticsys.event_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.Contants.EventVisibility;
import org.pqkkkkk.ticsys.event_service.client.identity.IdentityClient;
import org.pqkkkkk.ticsys.event_service.dao.jpa_repository.EventRepository;
import org.pqkkkkk.ticsys.event_service.entity.Event;
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
public class EventCreationSettingServiceTest {
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;
    @MockitoBean
    private IdentityClient identityClient;

    @Test
    public void createEventStepThree_WithNoEventVisibility_ShouldThrowException() {
        // Given
        Event event = Event.builder()
            .userId(1L)
            .eventName("Test Event")
            .eventDescription("This is a test event")
            .build();
        eventRepository.save(event);
        // When
        when(identityClient.isValidUser(event.getUserId())).thenReturn(true);
        // Act & Assert
        assertThrows(NotEnoughInfoException.class, () -> eventService.createEvent(event, EventCreationStep.SETTINGS.getStep()));
    }
    @Test
    public void createEventStepThree_WithNonExistEvent_ShouldThrowException() {
        // Given
        Event event = Event.builder()
            .userId(1L)
            .eventName("Test Event")
            .eventDescription("This is a test event")
            .eventVisibility(EventVisibility.PUBLIC) // Set visibility to avoid NotEnoughInfoException
            .build();
        // When
        when(identityClient.isValidUser(event.getUserId())).thenReturn(true);
        // Act & Assert
        assertThrows(EventNotExistException.class, () -> eventService.createEvent(event, EventCreationStep.SETTINGS.getStep()));
    }
    @Test
    public void createEventStepThree_WithValidData_ShouldReturnEventWithPendingStatus() {
        // Given
        Event event = Event.builder()
            .userId(1L)
            .eventName("Test Event")
            .eventDescription("This is a test event")
            .build();
        eventRepository.save(event);

        event.setEventVisibility(EventVisibility.PUBLIC);
        // When
        when(identityClient.isValidUser(event.getUserId())).thenReturn(true);
        // Act
        Event createdEvent = eventService.createEvent(event, EventCreationStep.SETTINGS.getStep());
        // Assert
        assertNotNull(createdEvent);
        assertEquals(EventStatus.PENDING, createdEvent.getEventStatus());
    }
}
