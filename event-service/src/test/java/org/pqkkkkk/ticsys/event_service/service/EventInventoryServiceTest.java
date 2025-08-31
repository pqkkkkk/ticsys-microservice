package org.pqkkkkk.ticsys.event_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketQuantity;
import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketReservation;
import org.pqkkkkk.ticsys.event_service.dao.TicketDao;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@Transactional
public class EventInventoryServiceTest {
    @Autowired
    private EventInventoryService eventInventoryService;
    @Autowired
    private TicketDao ticketDao;
    @MockitoBean
    private EventQueryService eventQueryService;

    @BeforeAll
    public void setUp() {
        // Initialize test data
        List<Ticket> tickets = List.of(
            Ticket.builder()
                .ticketPrice(50.0)
                .ticketName("Ticket 1").ticketTotalQuantity(5)
                .ticketStockQuantity(5).build(),
            Ticket.builder()
                .ticketPrice(25.0)
                .ticketName("Ticket 2").ticketTotalQuantity(3)
                .ticketStockQuantity(3).build()
        );
        ticketDao.saveAll(tickets);
    }

    @Test
    // Test cases for calculateTotalPrice
    public void calculateTotalPrice_WithValidTickets_ReturnsTotalPrice() {
        // Arrange
        Ticket ticket1 = Ticket.builder().ticketId(1L).ticketPrice(50.0).ticketStockQuantity(2).build();
        Ticket ticket2 = Ticket.builder().ticketId(2L).ticketPrice(25.0).ticketStockQuantity(2).build();
        List<Ticket> tickets = List.of(ticket1, ticket2);

        List<TicketQuantity> ticketQuantities = List.of(
                TicketQuantity.builder().ticketId(1L).quantity(1).build(),
                TicketQuantity.builder().ticketId(2L).quantity(2).build()
        );

        // Act
        Double totalPrice = eventInventoryService.calculateTotalPrice(ticketQuantities, tickets);

        // Assert
        assertNotNull(totalPrice);
        assertEquals(100.0, totalPrice);
    }

    // Test cases for checkTicketAvailability
    @Test
    public void checkTicketAvailability_WithTicketsSizeNotEquals_ShouldThrowException(){
        // Arrange
        List<TicketQuantity> ticketQuantities = List.of(
                TicketQuantity.builder().ticketId(1L).quantity(1).build(),
                TicketQuantity.builder().ticketId(2L).quantity(2).build(),
                TicketQuantity.builder().ticketId(3L).quantity(1).build()
        );
        TicketReservation ticketReservation = TicketReservation.builder()
                .ticketQuantities(ticketQuantities)
                .build();

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            eventInventoryService.checkTicketAvailability(ticketReservation);
        });
    }
    @Test
    public void checkTicketAvailability_WithNoAvailableTicketStock_ShouldThrowException(){
        // Arrange
        List<TicketQuantity> ticketQuantities = List.of(
                TicketQuantity.builder().ticketId(1L).quantity(6).build(),
                TicketQuantity.builder().ticketId(2L).quantity(2).build()
        );
        TicketReservation ticketReservation = TicketReservation.builder()
                .ticketQuantities(ticketQuantities)
                .build();

        // Act

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            eventInventoryService.checkTicketAvailability(ticketReservation);
        });
    }
    @Test
    public void checkTicketAvailability_WithAvailableTicketStock_ShouldReturnCorrespondingTickets(){
        // Arrange
        List<TicketQuantity> ticketQuantities = List.of(
                TicketQuantity.builder().ticketId(1L).quantity(2).build(),
                TicketQuantity.builder().ticketId(2L).quantity(1).build()
        );
        TicketReservation ticketReservation = TicketReservation.builder()
                .ticketQuantities(ticketQuantities)
                .build();

        // Act
        List<Ticket> availableTickets = eventInventoryService.checkTicketAvailability(ticketReservation);

        // Assert
        assertNotNull(availableTickets);
        assertEquals(2, availableTickets.size());
    }
    // Test cases for reserveTickets
    @Test
    public void reserveTickets_WithNotValidEventDate_ShouldThrowException(){
        // Arrange
        List<TicketQuantity> ticketQuantities = List.of(
                TicketQuantity.builder().ticketId(1L).quantity(2).build(),
                TicketQuantity.builder().ticketId(2L).quantity(1).build()
        );
        TicketReservation ticketReservation = TicketReservation.builder()
                .eventId(999L)
                .eventDateId(999L)
                .ticketQuantities(ticketQuantities)
                .build();

        // Act
        when(eventQueryService.isValidEventDate(ticketReservation.getEventId(), ticketReservation.getEventDateId())).thenReturn(false);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            eventInventoryService.reserveTickets(ticketReservation);
        });
    }
    @Test
    public void reserveTickets_WithValidEventDateAndAvailableTicketStock_ShouldReturnTotalPriceAndUpdateStockQuantity(){
        // Arrange
        List<TicketQuantity> ticketQuantities = List.of(
                TicketQuantity.builder().ticketId(1L).quantity(2).build(),
                TicketQuantity.builder().ticketId(2L).quantity(1).build()
        );
        TicketReservation ticketReservation = TicketReservation.builder()
                .eventId(1L)
                .eventDateId(1L)
                .ticketQuantities(ticketQuantities)
                .build();

        // Act
        when(eventQueryService.isValidEventDate(ticketReservation.getEventId(), ticketReservation.getEventDateId())).thenReturn(true);
        Double totalPrice = eventInventoryService.reserveTickets(ticketReservation);
        List<Ticket> ticketsAfterReservation = ticketDao.findTickets(ticketQuantities);

        // Assert
        assertNotNull(totalPrice);
        assertEquals(125.0, totalPrice);
        assertEquals(3, ticketsAfterReservation.get(0).getTicketStockQuantity());
        assertEquals(2, ticketsAfterReservation.get(1).getTicketStockQuantity());
    }
}
