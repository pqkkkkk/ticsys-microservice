package org.pqkkkkk.ticsys.event_service.dto.request;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.model.Event;
import org.pqkkkkk.ticsys.event_service.model.Ticket;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequest {
   Event event;
   List<Ticket> tickets;
}
