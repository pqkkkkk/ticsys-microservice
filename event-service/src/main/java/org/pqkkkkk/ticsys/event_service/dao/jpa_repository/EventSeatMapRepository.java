package org.pqkkkkk.ticsys.event_service.dao.jpa_repository;

import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSeatMapRepository extends JpaRepository<EventSeatMap, Long> {

}
