package org.pqkkkkk.ticsys.event_service.service;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.entity.EventMember;

public interface EventMemberService {
    public EventMember addEventMember(EventMember eventMemberInfo);
    public boolean removeEventMember(Long eventMemberId);
    public EventMember updateEventMember(EventMember eventMemberInfo);
    public List<EventMember> getAllEventMembersOfEvent(Long eventId);
}
