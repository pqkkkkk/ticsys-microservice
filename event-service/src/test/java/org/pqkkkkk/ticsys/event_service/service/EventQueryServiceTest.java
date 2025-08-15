package org.pqkkkkk.ticsys.event_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.pqkkkkk.ticsys.event_service.Contants.EventCategory;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.dto.filter_object.EventFilter;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class EventQueryServiceTest {
    @Autowired
    private EventQueryService eventQueryService;
    @Autowired
    EventCommandDao eventCommandDao;

    @BeforeAll
    public void setUp() {
        // Initialize any necessary data or state before each test
        Event event = Event.builder()
                .eventName("Test Event")
                .eventDescription("This is a test event")
                .userId(1L)
                .eventStatus(EventStatus.ON_GOING)
                .eventCategory(EventCategory.ART)
                .build();
        eventCommandDao.createEvent(event);
        
        Event event2 = Event.builder()
                .eventName("Another Test Event")
                .eventDescription("This is another test event")
                .userId(2L)
                .eventStatus(EventStatus.COMPLETED)
                .eventCategory(EventCategory.SPORTS)
                .build();
        eventCommandDao.createEvent(event2);

        Event event3 = Event.builder()
                .eventName("Future Event")
                .eventDescription("This is a future event")
                .eventStatus(EventStatus.PENDING)
                .eventCategory(EventCategory.MUSIC)
                .userId(1L)
                .build();
        eventCommandDao.createEvent(event3);
    }

    @Test
    public void getEvents_WithNoFilterValues_ReturnsAllEvents() {
        // Given
        EventFilter filter = EventFilter.builder()
                .currentPage(1)
                .pageSize(10)
                .build();
        // Act
        Page<Event> events = eventQueryService.getEvents(filter);

        // Assert
        assertEquals(3, events.getContent().size());
    }
    @Test
    public void getEvents_WithStatusFilter_ReturnsFilteredEvents() {
        // Given
        EventFilter filter = EventFilter.builder()
                .currentPage(1)
                .pageSize(10)
                .eventStatus(EventStatus.ON_GOING)
                .build();
        // Act
        Page<Event> events = eventQueryService.getEvents(filter);

        // Assert
        assertEquals(1, events.getContent().size());
    }
    @Test
    public void getEvents_WithCategoryFilter_ReturnsFilteredEvents() {
        // Given
        EventFilter filter = EventFilter.builder()
                .currentPage(1)
                .pageSize(10)
                .eventCategory(EventCategory.ART)
                .build();
        // Act
        Page<Event> events = eventQueryService.getEvents(filter);

        // Assert
        assertEquals(1, events.getContent().size());
    }
    @Test
    public void getEvents_WithUserIdFilter_ReturnsFilteredEvents() {
        // Given
        EventFilter filter = EventFilter.builder()
                .currentPage(1)
                .pageSize(10)
                .userId(1L)
                .build();
        // Act
        Page<Event> events = eventQueryService.getEvents(filter);

        // Assert
        assertEquals(2, events.getContent().size());
    }
}