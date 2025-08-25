package org.pqkkkkk.ticsys.event_service.dao.jpa_repository;

import org.pqkkkkk.ticsys.event_service.dto.filter_object.EventFilter;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = """
            SELECT DISTINCT e FROM Event e
            LEFT JOIN FETCH e.eventOrganizer
            LEFT JOIN FETCH e.eventOfflineLocation
            LEFT JOIN FETCH e.eventOnlineLocation
            WHERE (:#{#eventFillter.eventNameKeyword} IS NULL OR e.eventName LIKE CONCAT('%', :#{#eventFillter.eventNameKeyword}, '%'))
            AND (:#{#eventFillter.eventStatus} IS NULL OR e.eventStatus = :#{#eventFillter.eventStatus})
            AND (:#{#eventFillter.eventCategory} IS NULL OR e.eventCategory = :#{#eventFillter.eventCategory})
            AND (:#{#eventFillter.eventLocationType} IS NULL OR e.eventLocationType = :#{#eventFillter.eventLocationType})
            AND (:#{#eventFillter.userId} IS NULL OR e.userId = :#{#eventFillter.userId})
        """
        )
    public Page<Event> findByEventFillter(EventFilter eventFillter, Pageable pageable);
}
