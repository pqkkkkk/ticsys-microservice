package org.pqkkkkk.ticsys.event_service.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketQuantity;
import org.pqkkkkk.ticsys.event_service.dao.jpa_repository.TicketRepository;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;
import org.springframework.stereotype.Repository;

@Repository
public class TicketJpaDao implements TicketDao {
    private TicketRepository ticketRepository;

    public TicketJpaDao(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> findTickets(List<TicketQuantity> ticketQuantities) {
        return ticketRepository.findAllById(ticketQuantities.stream()
                .map(TicketQuantity::getTicketId)
                .collect(Collectors.toList()));
    }

    @Override
    public boolean saveAll(List<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
        return true;
    }

}
