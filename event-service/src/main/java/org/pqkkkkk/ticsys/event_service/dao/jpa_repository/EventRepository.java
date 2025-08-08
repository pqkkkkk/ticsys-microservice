package org.pqkkkkk.ticsys.event_service.dao.jpa_repository;

import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
