package org.pqkkkkk.ticsys.event_service.dao;

import java.util.List;

import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketQuantity;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;

public interface TicketDao {
    public List<Ticket> findTickets(List<TicketQuantity> ticketQuantities);
    public boolean saveAll(List<Ticket> tickets);
}
