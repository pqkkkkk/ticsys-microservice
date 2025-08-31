package org.pqkkkkk.ticsys.event_service.dao.jpa_repository;

import org.pqkkkkk.ticsys.event_service.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
