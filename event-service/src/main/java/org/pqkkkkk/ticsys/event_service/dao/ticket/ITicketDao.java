package org.pqkkkkk.ticsys.event_service.dao.ticket;

import java.util.List;
import java.util.Map;

import org.pqkkkkk.ticsys.event_service.model.Ticket;

public interface ITicketDao {
    boolean AddTicket(Ticket ticket);
    List<Ticket> GetTicketsOfEvent(int eventId);
    Map<String, Object> GetTicketByRequiredFieldsList(List<String> requiredFields, int id);
    Ticket GetTicketById(int id);
    int UpdateTicketByRequiredFieldsList(Map<String, Object> newValues, int id);
}
