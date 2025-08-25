package org.pqkkkkk.ticsys.event_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.event_service.Contants.EventCategory;
import org.pqkkkkk.ticsys.event_service.Contants.EventLocationType;
import org.pqkkkkk.ticsys.event_service.client.identity.IdentityClient;
import org.pqkkkkk.ticsys.event_service.dto.request.EventRequest.CreateEventOfflineLocationRequest;
import org.pqkkkkk.ticsys.event_service.dto.request.EventRequest.CreateEventOnlineLocationRequest;
import org.pqkkkkk.ticsys.event_service.dto.request.EventRequest.CreateEventOrganizerRequest;
import org.pqkkkkk.ticsys.event_service.dto.request.EventRequest.CreateEventRequest;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.exception.NotEnoughInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventCreationBasicInfoServiceTest {
    @Autowired
    private EventService eventService;

    @MockitoBean
    private IdentityClient identityClient;
    
    // ------------- Test cases for create event method -------------------------------
    @Test
    public void createEventStepOne_WithValidDataOfOnlineLocation_ShouldReturnEvent() {
        // Given
        CreateEventOrganizerRequest organizerRequest = new CreateEventOrganizerRequest("Organization", null);
        CreateEventOnlineLocationRequest onlineLocationRequest = new CreateEventOnlineLocationRequest("adsadsad", "asdasdasd", "123");

        CreateEventRequest request = new CreateEventRequest(null,1, Long.valueOf(1), "Event",
         "Event Description", EventCategory.MUSIC, null, EventLocationType.ONLINE, null,
        organizerRequest, null, null, null, onlineLocationRequest);

        // When
        when(identityClient.isValidUser(request.userId())).thenReturn(true);
        Event event = eventService.createEvent(request.toEntity(), request.currentStep());

        // Assert
        assertNotNull(event);
        assertEquals("Event", event.getEventName());
        assertNotNull(event.getEventOnlineLocation().getEventOnlineLocationId());
        assertNotNull(event.getEventOrganizer().getOrganizerId());
        assertNotNull(event.getEventMembers());
        assertFalse(event.getEventMembers().isEmpty());
    }
    @Test
    public void createEventStepOne_WithValidDataOfOfflineLocation_ShouldReturnEvent() {
        // Given
        CreateEventOrganizerRequest organizerRequest = new CreateEventOrganizerRequest("Organization", null);
        CreateEventOfflineLocationRequest offlineLocationRequest = new CreateEventOfflineLocationRequest("Province", "Ward", "Street", "Venue");

        CreateEventRequest request = new CreateEventRequest(null,1, Long.valueOf(1), "Event",
         "Event Description", EventCategory.MUSIC, null, EventLocationType.OFFLINE, null,
        organizerRequest, null, null, offlineLocationRequest, null);

        // When
        when(identityClient.isValidUser(request.userId())).thenReturn(true);
        Event event = eventService.createEvent(request.toEntity(), request.currentStep());

        // Assert
        assertNotNull(event);
        assertEquals("Event", event.getEventName());
        assertNotNull(event.getEventOfflineLocation().getEventOfflineLocationId());
        assertNotNull(event.getEventOrganizer().getOrganizerId());
        assertNotNull(event.getEventMembers());
        assertFalse(event.getEventMembers().isEmpty());
    }
    @Test
    public void createEventStepOne_WithValidOfflineLocationButOnlineLocationType_ShouldThrowException() {
        // Given
        CreateEventOrganizerRequest organizerRequest = new CreateEventOrganizerRequest("Organization", null);
        CreateEventOfflineLocationRequest offlineLocationRequest = new CreateEventOfflineLocationRequest("Province", "Ward", "Street", "Venue");

        CreateEventRequest request = new CreateEventRequest(null,1, Long.valueOf(1), "Event",
         "Event Description", EventCategory.MUSIC, null, EventLocationType.ONLINE, null,
        organizerRequest, null, null, offlineLocationRequest, null);

        // When
        when(identityClient.isValidUser(request.userId())).thenReturn(true);

        // Then
        assertThrows(NotEnoughInfoException.class, () -> {
            eventService.createEvent(request.toEntity(), request.currentStep());
        });
    }
    @Test
    public void createEventStepOne_WithBothOfflineAndOnlineLocation_ShouldThrowException() {
        // Given
        CreateEventOrganizerRequest organizerRequest = new CreateEventOrganizerRequest("Organization", null);
        CreateEventOfflineLocationRequest offlineLocationRequest = new CreateEventOfflineLocationRequest("Province", "Ward", "Street", "Venue");
        CreateEventOnlineLocationRequest onlineLocationRequest = new CreateEventOnlineLocationRequest("Link", "ID", "Password");

        CreateEventRequest request = new CreateEventRequest(null,1, Long.valueOf(1), "Event",
         "Event Description", EventCategory.MUSIC, null, EventLocationType.ONLINE, null,
        organizerRequest, null, null, offlineLocationRequest, onlineLocationRequest);

        // When
        when(identityClient.isValidUser(request.userId())).thenReturn(true);

        // Then
        assertThrows(NotEnoughInfoException.class, () -> {
            eventService.createEvent(request.toEntity(), request.currentStep());
        });
    }
}
