package org.pqkkkkk.ticsys.event_service.service.impl;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.client.identity.IdentityClient;
import org.pqkkkkk.ticsys.event_service.dao.EventMemberDao;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.exception.UserIsNotValidException;
import org.pqkkkkk.ticsys.event_service.service.EventMemberService;
import org.pqkkkkk.ticsys.event_service.service.EventQueryService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EventMemberServiceImpl implements EventMemberService {
    private final EventMemberDao eventMemberDao;
    private final IdentityClient identityClient;
    private final EventQueryService eventQueryService;

    public EventMemberServiceImpl(EventMemberDao eventMemberDao,
                            IdentityClient identityClient, EventQueryService eventService) {
        this.eventMemberDao = eventMemberDao;
        this.identityClient = identityClient;
        this.eventQueryService = eventService;
    }

    @Override
    @Transactional
    public EventMember addEventMember(EventMember eventMemberInfo) {
        if(eventMemberInfo == null) {
            throw new IllegalArgumentException("Event member information cannot be null");
        }
        if(eventMemberInfo.getEvent() == null || eventMemberInfo.getEvent().getEventId() == null) {
            throw new IllegalArgumentException("Event information cannot be null");
        }
        if(!eventQueryService.isValidEvent(eventMemberInfo.getEvent().getEventId())) {
            throw new EventNotExistException(eventMemberInfo.getEvent().getEventId());
        }
        if(!identityClient.isValidUser(eventMemberInfo.getUserId())) {
            throw new UserIsNotValidException("Invalid user with ID: " + eventMemberInfo.getUserId());
        }

        return eventMemberDao.addEventMember(eventMemberInfo);
    }

    @Override
    @Transactional
    public boolean removeEventMember(Long eventMemberId) {
        if(eventMemberId == null){
            throw new IllegalArgumentException("Event member ID cannot be null");
        }
        
        boolean result = eventMemberDao.removeEventMember(eventMemberId);

        if(!result){
            throw new IllegalArgumentException("No exist event member with ID" + eventMemberId);
        }

        return result;
    }

    @Override
    @Transactional
    public EventMember updateEventMember(EventMember eventMemberInfo) {
        if(eventMemberInfo == null) {
            throw new IllegalArgumentException("Event member information cannot be null");
        }
        if(eventMemberInfo.getEvent() == null || eventMemberInfo.getEvent().getEventId() == null) {
            throw new IllegalArgumentException("Event information cannot be null");
        }
        if(!eventQueryService.isValidEvent(eventMemberInfo.getEvent().getEventId())) {
            throw new EventNotExistException(eventMemberInfo.getEvent().getEventId());
        }

        EventMember eventMember = eventMemberDao.updateEventMember(eventMemberInfo);
        
        return eventMember;
    }

    @Override
    public List<EventMember> getAllEventMembersOfEvent(Long eventId) {
        return eventMemberDao.getAllEventMembers(eventId);
    }

}
