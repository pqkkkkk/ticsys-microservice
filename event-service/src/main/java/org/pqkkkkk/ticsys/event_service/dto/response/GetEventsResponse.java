package org.pqkkkkk.ticsys.event_service.dto.response;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.dto.EventDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetEventsResponse {
    List<EventDto> eventDtos;
    String message;
}
