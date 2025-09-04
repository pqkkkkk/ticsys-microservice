package org.pqkkkkk.ticsys.event_service.dao.jpa_repository;

import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDateRepository extends JpaRepository<EventDate, Long> {

}
