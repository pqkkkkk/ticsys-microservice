package org.pqkkkkk.ticsys.event_service.dto.response;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.dto.TimelyEventDataDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TimelyEventTicketCountResponse {
    List<TimelyEventDataDto> ticketCounts;
    Integer totalTicketCount;
    String message;
}
