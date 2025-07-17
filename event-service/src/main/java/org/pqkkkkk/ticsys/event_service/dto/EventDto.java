package org.pqkkkkk.ticsys.event_service.dto;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.model.Event;
import org.pqkkkkk.ticsys.event_service.model.Ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    Event event;
    List<Ticket> tickets;
    int minPriceOfTicket;
}
