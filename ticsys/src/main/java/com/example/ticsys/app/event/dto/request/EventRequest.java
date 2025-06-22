package com.example.ticsys.app.event.dto.request;

import java.util.List;

import com.example.ticsys.app.event.model.Event;
import com.example.ticsys.app.event.model.Ticket;

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
