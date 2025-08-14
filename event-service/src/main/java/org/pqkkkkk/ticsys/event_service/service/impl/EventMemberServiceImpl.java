package org.pqkkkkk.ticsys.event_service.service.impl;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.dao.EventMemberDao;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.pqkkkkk.ticsys.event_service.service.EventMemberService;
import org.springframework.stereotype.Service;

@Service
public class EventMemberServiceImpl implements EventMemberService {
    private final EventMemberDao eventMemberDao;

    public EventMemberServiceImpl(EventMemberDao eventMemberDao) {
        this.eventMemberDao = eventMemberDao;
    }

    @Override
    public EventMember addEventMember(EventMember eventMemberInfo) {
        return eventMemberDao.addEventMember(eventMemberInfo);
    }

    @Override
    public boolean removeEventMember(Long eventMemberId) {
        if(eventMemberId == null){
            throw new NullPointerException("Event member ID cannot be null");
        }
        
        boolean result = eventMemberDao.removeEventMember(eventMemberId);

        if(!result){
            throw new IllegalArgumentException("No exist event member with ID" + eventMemberId);
        }

        return result;
    }

    @Override
    public EventMember updateEventMember(EventMember eventMemberInfo) {
        EventMember eventMember = eventMemberDao.updateEventMember(eventMemberInfo);
        
        return eventMember;
    }

    @Override
    public List<EventMember> getAllEventMembersOfEvent(Long eventId) {
        return eventMemberDao.getAllEventMembers(eventId);
    }

}
