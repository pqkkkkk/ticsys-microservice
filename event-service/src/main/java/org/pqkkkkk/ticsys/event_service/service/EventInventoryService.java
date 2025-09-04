package org.pqkkkkk.ticsys.event_service.service;

import java.util.List;

import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketQuantity;
import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketReservation;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;

public interface EventInventoryService {
    public Double reserveTickets(TicketReservation ticketReservation);
    public Double calculateTotalPrice(List<TicketQuantity> ticketQuantities, List<Ticket> correspondingTickets);
    public boolean releaseTickets(TicketReservation ticketReservation);
    public List<Ticket> checkTicketAvailability(TicketReservation ticketReservation);
}
