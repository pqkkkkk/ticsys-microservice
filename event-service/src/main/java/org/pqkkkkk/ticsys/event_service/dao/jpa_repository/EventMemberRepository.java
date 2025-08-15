package org.pqkkkkk.ticsys.event_service.dao.jpa_repository;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventMemberRepository extends JpaRepository<EventMember, Long>  {
    public List<EventMember> findByEventEventId(Long eventId);
    @Modifying
    @Query("""
        DELETE FROM EventMember em WHERE em.eventMemberId = :eventMemberId
        """)
    public Integer deleteByEventMemberId(Long eventMemberId);
}
