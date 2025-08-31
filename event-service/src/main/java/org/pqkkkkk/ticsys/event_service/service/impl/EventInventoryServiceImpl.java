package org.pqkkkkk.ticsys.event_service.service.impl;

import java.util.List;

import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketQuantity;
import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketReservation;
import org.pqkkkkk.ticsys.event_service.dao.TicketDao;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;
import org.pqkkkkk.ticsys.event_service.service.EventInventoryService;
import org.pqkkkkk.ticsys.event_service.service.EventQueryService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EventInventoryServiceImpl implements EventInventoryService {
    private final EventQueryService eventQueryService;
    private final TicketDao ticketDao;

    public EventInventoryServiceImpl(EventQueryService eventQueryService, TicketDao ticketDao) {
        this.eventQueryService = eventQueryService;
        this.ticketDao = ticketDao;
    }

    @Override
    @Transactional
    public Double reserveTickets(TicketReservation ticketReservation) {
        if(!eventQueryService.isValidEventDate(ticketReservation.getEventId(), ticketReservation.getEventDateId())){
            throw new IllegalArgumentException("Invalid event");
        }

        List<Ticket> correspondingTickets = checkTicketAvailability(ticketReservation);

        for(int i = 0; i < correspondingTickets.size(); i++){
            Ticket ticket = correspondingTickets.get(i);
            ticket.setTicketStockQuantity(ticket.getTicketStockQuantity() - ticketReservation.getTicketQuantities().get(i).getQuantity());
        }

        boolean saved = ticketDao.saveAll(correspondingTickets);

        return saved ? calculateTotalPrice(ticketReservation.getTicketQuantities(), correspondingTickets) : null;
    }

    @Override
    @Transactional
    public boolean releaseTickets(TicketReservation ticketReservation) {
        List<Ticket> correspondingTickets = ticketDao.findTickets(ticketReservation.getTicketQuantities());

        if(correspondingTickets.size() != ticketReservation.getTicketQuantities().size()){
            throw new IllegalArgumentException("Some tickets not found");
        }

        for(int i = 0; i < correspondingTickets.size(); i++){
            Ticket ticket = correspondingTickets.get(i);
            ticket.setTicketStockQuantity(ticket.getTicketStockQuantity() + ticketReservation.getTicketQuantities().get(i).getQuantity());
        }

        boolean saved = ticketDao.saveAll(correspondingTickets);

        return saved;
    }

    @Override
    public List<Ticket> checkTicketAvailability(TicketReservation ticketReservation) {
        List<Ticket> correspondingTickets = ticketDao.findTickets(ticketReservation.getTicketQuantities());

        if(correspondingTickets.size() != ticketReservation.getTicketQuantities().size()){
            throw new IllegalArgumentException("Some tickets not found");
        }

        for(int i = 0; i < correspondingTickets.size(); i++){
            Ticket ticket = correspondingTickets.get(i);
            if(ticket.getTicketStockQuantity() < ticketReservation.getTicketQuantities().get(i).getQuantity()){
                throw new IllegalArgumentException("Not enough stock for ticket id: " + ticket.getTicketId());
            }
        }

        return correspondingTickets;
    }

    @Override
    public Double calculateTotalPrice(List<TicketQuantity> ticketQuantities, List<Ticket> correspondingTickets) {
        Double totalPrice = 0.0;

        for (int i = 0; i < ticketQuantities.size(); i++) {
            TicketQuantity ticketQuantity = ticketQuantities.get(i);
            Ticket ticket = correspondingTickets.get(i);

            totalPrice += ticket.getTicketPrice() * ticketQuantity.getQuantity();
        }

        return totalPrice;
    }

}
