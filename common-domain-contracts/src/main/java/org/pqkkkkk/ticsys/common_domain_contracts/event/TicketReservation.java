package org.pqkkkkk.ticsys.common_domain_contracts.event;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketReservation {
    Long eventId;
    Long eventDateId;
    List<TicketQuantity> ticketQuantities;
}
