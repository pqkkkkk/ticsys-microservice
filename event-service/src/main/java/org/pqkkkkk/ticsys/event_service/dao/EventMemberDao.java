package org.pqkkkkk.ticsys.event_service.dao;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.entity.EventMember;

public interface EventMemberDao {
    public EventMember addEventMember(EventMember eventMemberInfo);
    public boolean removeEventMember(Long eventMemberId);
    public EventMember updateEventMember(EventMember eventMemberInfo);
    public List<EventMember> getAllEventMembers(Long eventId);
}
