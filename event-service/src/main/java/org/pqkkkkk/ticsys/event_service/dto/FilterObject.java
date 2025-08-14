package org.pqkkkkk.ticsys.event_service.dto;

import org.pqkkkkk.ticsys.event_service.Contants.EventCategory;
import org.pqkkkkk.ticsys.event_service.Contants.EventLocationType;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.springframework.data.domain.Sort.Direction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class FilterObject {
    public record EventFillter(
        @NotNull(message = "Current page must not be null")
        @Min(value = 1, message = "Current page must be greater than or equal to 1")
        Integer currentPage,

        @NotNull(message = "Page size must not be null")
        @Min(value = 1, message = "Page size must be greater than or equal to 1")
        Integer pageSize,

        @Pattern(regexp = "^(eventId|eventName|createdAt|updatedAt)$", message = "Invalid sortBy value")
        String sortBy,

        Direction sortDirection,
        Long userId,
        String eventNameKeyword,
        EventStatus eventStatus,
        EventCategory eventCategory,
        EventLocationType eventLocationType
    ){}
}
