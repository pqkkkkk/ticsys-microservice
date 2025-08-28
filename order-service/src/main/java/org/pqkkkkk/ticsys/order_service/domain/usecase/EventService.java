package org.pqkkkkk.ticsys.order_service.domain.usecase;

import java.util.List;

import org.pqkkkkk.ticsys.order_service.domain.entity.TicketInOrder;

public interface EventService {
    public boolean isValidEvent(Long eventId);
    public Double reverseTicketsAndCalculateSubtotal(Long eventId, List<TicketInOrder> ticketInOrders);
}
