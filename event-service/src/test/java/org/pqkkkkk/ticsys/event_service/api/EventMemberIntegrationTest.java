package org.pqkkkkk.ticsys.event_service.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.Contants.UserRoleInEvent;
import org.pqkkkkk.ticsys.event_service.client.identity.IdentityClient;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.dao.EventMemberDao;
import org.pqkkkkk.ticsys.event_service.dto.DTO.EventMemberDTO;
import org.pqkkkkk.ticsys.event_service.dto.Response.ApiResponse;
import org.pqkkkkk.ticsys.event_service.dto.request.EventMemberRequest.CreateEventMemberRequest;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EventMemberIntegrationTest {
    private final static String BASE_URL = "/api/v1/event/member";
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private EventCommandDao eventCommandDao;
    @Autowired
    private EventMemberDao eventMemberDao;
    @MockitoBean
    private IdentityClient identityClient;

    @Test
    public void createEventMember_WithValidData_ShouldReturnCreated(){
        // Given
        Event eventInfo = Event.builder()
                .eventName("Test Event")
                .eventStatus(EventStatus.ON_GOING)
                .userId(1L)
                .build();
        eventCommandDao.createEvent(eventInfo);

        CreateEventMemberRequest request = new CreateEventMemberRequest(1L, eventInfo.getEventId(),
                                UserRoleInEvent.MEMBER);
        
        // When
        when(identityClient.isValidUser(request.userId())).thenReturn(true);
        ResponseEntity<ApiResponse<EventMemberDTO>> response = restTemplate.postForEntity(BASE_URL,
                request, (Class<ApiResponse<EventMemberDTO>>) (Class<?>) ApiResponse.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @Test
    public void updateEventMember_WithValidData_ShouldReturnOK(){
            // Given
        Event eventInfo = Event.builder()
                .eventName("Test Event")
                .eventStatus(EventStatus.ON_GOING)
                .userId(1L)
                .build();

        eventCommandDao.createEvent(eventInfo);

        EventMember eventMember = EventMember.builder()
                .userId(1L)
                .event(eventInfo)
                .userRoleInEvent(UserRoleInEvent.ADMIN)
                .build();
        eventMemberDao.addEventMember(eventMember);

        CreateEventMemberRequest request = new CreateEventMemberRequest(1L, eventInfo.getEventId(),
                UserRoleInEvent.MEMBER);

        // When
        when(identityClient.isValidUser(request.userId())).thenReturn(true);
        
        // Using exchange() for PUT request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateEventMemberRequest> requestEntity = new HttpEntity<>(request, headers);
        
        ResponseEntity<ApiResponse<EventMemberDTO>> response = restTemplate.exchange(
                BASE_URL + "/" + eventMember.getEventMemberId(),
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<ApiResponse<EventMemberDTO>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserRoleInEvent.MEMBER, response.getBody().data().role());
    }
    @Test
    public void deleteEventMember_WithValidData_ShouldReturnNoContent() {
        // Given
        Event eventInfo = Event.builder()
                .eventName("Test Event")
                .eventStatus(EventStatus.ON_GOING)
                .userId(1L)
                .build();
        eventCommandDao.createEvent(eventInfo);

        EventMember eventMember = EventMember.builder()
                .userId(1L)
                .event(eventInfo)
                .userRoleInEvent(UserRoleInEvent.ADMIN)
                .build();
        eventMemberDao.addEventMember(eventMember);

        // When
        when(identityClient.isValidUser(eventMember.getUserId())).thenReturn(true);
        
        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URL + "/" + eventMember.getEventMemberId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
