package org.pqkkkkk.ticsys.event_service.dto.filter_object;

import org.pqkkkkk.ticsys.event_service.Contants.EventCategory;
import org.pqkkkkk.ticsys.event_service.Contants.EventLocationType;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.springframework.data.domain.Sort.Direction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class EventFilter {
    @NotNull(message = "Current page must not be null")
    @Min(value = 1, message = "Current page must be greater than or equal to 1")
    Integer currentPage;

    @NotNull(message = "Page size must not be null")
    @Min(value = 1, message = "Page size must be greater than or equal to 1")
    Integer pageSize;

    @Pattern(regexp = "^(eventId|eventName|eventCreatedAt|eventUpdatedAt)$", message = "Invalid sortBy value")
    @Builder.Default
    String sortBy = "eventCreatedAt";

    @Builder.Default
    Direction sortDirection = Direction.DESC;
    Long userId;
    String eventNameKeyword;
    EventStatus eventStatus;
    EventCategory eventCategory;
    EventLocationType eventLocationType;
}
