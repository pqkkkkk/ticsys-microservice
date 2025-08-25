package org.pqkkkkk.ticsys.event_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.Contants.UserRoleInEvent;
import org.pqkkkkk.ticsys.event_service.client.identity.IdentityClient;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.dao.EventMemberDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.exception.UserIsNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventMemberServiceTest {
    @Autowired
    private EventMemberService service;
    @Autowired
    private EventCommandDao eventCommandDao;
    @Autowired
    private EventMemberDao eventMemberDao;
    @MockitoBean
    private IdentityClient identityClient;

    // ----------------------- test cases for addMember --------------------------------
    @Test
    public void addMember_WithNullData_ShouldThrowException() {
        // Given
        EventMember memberInfo = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.addEventMember(memberInfo);
        });
    }
    @Test
    public void addMember_WithNoOnGoingEvent_ShouldThrowException(){
        // Given
        Event event = Event.builder()
                .eventName("Test Event")
                .userId(2L)
                .build();
        eventCommandDao.createEvent(event);

        EventMember memberInfo = new EventMember();
        memberInfo.setEvent(event);
        memberInfo.setUserId(1L);
        memberInfo.setUserRoleInEvent(UserRoleInEvent.MEMBER);

        // When
        when(identityClient.isValidUser(memberInfo.getUserId())).thenReturn(true);

        assertThrows(EventNotExistException.class, () -> {
            service.addEventMember(memberInfo);
        });
    }
    @Test
    public void addMember_WithInvalidUser_ShouldThrowException() {
        // Given
        Event event = Event.builder()
                .eventName("Test Event")
                .eventStatus(EventStatus.ON_GOING)
                .userId(2L)
                .build();
        eventCommandDao.createEvent(event);

        EventMember memberInfo = new EventMember();
        memberInfo.setEvent(event);
        memberInfo.setUserId(1L);
        memberInfo.setUserRoleInEvent(UserRoleInEvent.MEMBER);

        // When
        when(identityClient.isValidUser(memberInfo.getUserId())).thenReturn(false);

        assertThrows(UserIsNotValidException.class, () -> {
            service.addEventMember(memberInfo);
        });
    }
    @Test
    public void addMember_WithValidData_ShouldAddMember() {
        // Given
        Event event = Event.builder()
                .eventName("Test Event")
                .userId(2L)
                .eventStatus(EventStatus.ON_GOING)
                .build();
        eventCommandDao.createEvent(event);

        EventMember memberInfo = new EventMember();
        memberInfo.setEvent(event);
        memberInfo.setUserId(1L);
        memberInfo.setUserRoleInEvent(UserRoleInEvent.MEMBER);

        // When
        when(identityClient.isValidUser(memberInfo.getUserId())).thenReturn(true);
        EventMember addedMember = service.addEventMember(memberInfo);

        // Then
        assertNotNull(addedMember);
    }

    // ----------------------- test cases for removeEventMember ------------------------------------
    @Test
    public void removeEventMember_WithNullId_ShouldThrowException() {
        // Given
        Long eventMemberId = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.removeEventMember(eventMemberId);
        });
    }
    @Test
    public void removeEventMember_WithNonExistId_ShouldThrowException() {
        // Given
        Long eventMemberId = 999L;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.removeEventMember(eventMemberId);
        });
    }
    @Test
    public void removeEventMember_WithValidId_ShouldRemoveMember() {
        // Given
        Event event = Event.builder()
                .eventName("Test Event")
                .userId(2L)
                .eventStatus(EventStatus.ON_GOING)
                .build();
        eventCommandDao.createEvent(event);

        EventMember memberInfo = new EventMember();
        memberInfo.setEvent(event);
        memberInfo.setUserId(1L);
        memberInfo.setUserRoleInEvent(UserRoleInEvent.MEMBER);
        EventMember addedMember = eventMemberDao.addEventMember(memberInfo);

        // When
        boolean result = service.removeEventMember(addedMember.getEventMemberId());

        // Then
        assertNotNull(result);
    }
    // ----------------------- test cases for updateEventMember ------------------------------------
    @Test
    public void updateEventMember_WithNullInfo_ShouldThrowException() {
        // Given
        EventMember eventMemberInfo = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.updateEventMember(eventMemberInfo);
        });
    }

    @Test
    public void updateEventMember_WithInvalidEvent_ShouldThrowException() {
        // Given
        Event event = Event.builder()
                .eventName("Test Event")
                .userId(2L)
                .build();
        eventCommandDao.createEvent(event);

        EventMember memberInfo = new EventMember();
        memberInfo.setEvent(event);
        memberInfo.setUserId(1L);
        memberInfo.setUserRoleInEvent(UserRoleInEvent.MEMBER);

        // When
        when(identityClient.isValidUser(memberInfo.getUserId())).thenReturn(true);

        assertThrows(EventNotExistException.class, () -> {
            service.updateEventMember(memberInfo);
        });
    }

    @Test
    public void updateEventMember_WithNoExistEventMember_ShouldReturnNull() {
        // Given
        Event event = Event.builder()
                .eventName("Test Event")
                .userId(2L)
                .eventStatus(EventStatus.ON_GOING)
                .build();
        eventCommandDao.createEvent(event);

        EventMember memberInfo = new EventMember();
        memberInfo.setEvent(event);
        memberInfo.setUserId(1L);
        memberInfo.setUserRoleInEvent(UserRoleInEvent.MEMBER);

        // When
        EventMember updatedMember = service.updateEventMember(memberInfo);

        // Then
        assertNull(updatedMember);
    }
    @Test
    public void updateEventMember_WithValidData_ShouldUpdateMember() {
        // Given
        Event event = Event.builder()
                .eventName("Test Event")
                .userId(2L)
                .eventStatus(EventStatus.ON_GOING)
                .build();
        eventCommandDao.createEvent(event);

        EventMember existEventMember = new EventMember();
        existEventMember.setEvent(event);
        existEventMember.setUserId(1L);
        existEventMember.setUserRoleInEvent(UserRoleInEvent.MEMBER);
        eventMemberDao.addEventMember(existEventMember);

        EventMember memberInfo = new EventMember();
        memberInfo.setEventMemberId(existEventMember.getEventMemberId());
        memberInfo.setEvent(event);
        memberInfo.setUserId(1L);
        memberInfo.setUserRoleInEvent(UserRoleInEvent.ADMIN);

        // When
        EventMember updatedMember = service.updateEventMember(memberInfo);

        // Then
        assertNotNull(updatedMember);
        assertEquals(UserRoleInEvent.ADMIN, updatedMember.getUserRoleInEvent());
    }
    
    // ----------------------- test cases for getAllEventMembersOfEvent ------------------------------
    @Test
    public void getAllEventMembersOfEvent_WithValidEventId_ShouldReturnMembers() {
        // Given
        Event event = Event.builder()
                .eventName("Test Event")
                .userId(2L)
                .eventStatus(EventStatus.ON_GOING)
                .build();
        eventCommandDao.createEvent(event);

        EventMember memberInfo1 = new EventMember();
        memberInfo1.setEvent(event);
        memberInfo1.setUserId(1L);
        memberInfo1.setUserRoleInEvent(UserRoleInEvent.MEMBER);
        eventMemberDao.addEventMember(memberInfo1);

        EventMember memberInfo2 = new EventMember();
        memberInfo2.setEvent(event);
        memberInfo2.setUserId(3L);
        memberInfo2.setUserRoleInEvent(UserRoleInEvent.MEMBER);
        eventMemberDao.addEventMember(memberInfo2);

        // When
        List<EventMember> members = service.getAllEventMembersOfEvent(event.getEventId());

        // Then
        assertNotNull(members);
        assertEquals(2, members.size());
    }
}
