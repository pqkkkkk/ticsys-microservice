package org.pqkkkkk.ticsys.event_service.dao.jpa_repository;

import org.pqkkkkk.ticsys.event_service.dto.FilterObject.EventFillter;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = """
            SELECT e FROM Event e
            LEFT JOIN FETCH e.eventDates ed
            LEFT JOIN FETCH ed.tickets 
            LEFT JOIN FETCH e.eventFAQs
            LEFT JOIN FETCH e.eventOrganizer
            LEFT JOIN FETCH e.eventOfflineLocation
            LEFT JOIN FETCH e.eventOnlineLocation
            WHERE (:#{#eventFillter.eventId} IS NULL OR e.eventId = :#{#eventFillter.eventId})
            AND (:#{#eventFillter.eventNameKeyword} IS NULL OR e.eventName LIKE CONCAT('%', :#{#eventFillter.eventNameKeyword}, '%'))
            AND (:#{#eventFillter.eventStatus} IS NULL OR e.eventStatus = :#{#eventFillter.eventStatus})
            AND (:#{#eventFillter.eventCategory} IS NULL OR e.eventCategory = :#{#eventFillter.eventCategory})
            AND (:#{#eventFillter.eventLocationType} IS NULL OR e.eventLocationType = :#{#eventFillter.eventLocationType})    
            """)
    public Page<Event> findByEventFillter(EventFillter eventFillter, Pageable pageable);
}
