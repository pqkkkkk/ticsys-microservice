package org.pqkkkkk.ticsys.event_service.dao;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.dao.jpa_repository.EventMemberRepository;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.springframework.stereotype.Repository;

@Repository
public class EventMemberJpaDao implements EventMemberDao {
    private final EventMemberRepository eventMemberRepository;

    public EventMemberJpaDao(EventMemberRepository eventMemberRepository) {
        this.eventMemberRepository = eventMemberRepository;
    }

    @Override
    public EventMember addEventMember(EventMember eventMemberInfo) {
        if(eventMemberInfo.getEventMemberId() != null){
            return null;
        }

        return eventMemberRepository.save(eventMemberInfo);
    }

    @Override
    public boolean removeEventMember(Long eventMemberId) {
        Integer rowAffected = eventMemberRepository.deleteByEventMemberId(eventMemberId);
        return rowAffected > 0;
    }

    @Override
    public List<EventMember> getAllEventMembers(Long eventId) {
        return eventMemberRepository.findByEventEventId(eventId);
    }

    @Override
    public EventMember updateEventMember(EventMember eventMemberInfo) {
        if(eventMemberInfo == null || eventMemberInfo.getEventMemberId() == null || eventMemberRepository.findById(eventMemberInfo.getEventMemberId()) == null)
            return null;

        return eventMemberRepository.save(eventMemberInfo);
    }

}
