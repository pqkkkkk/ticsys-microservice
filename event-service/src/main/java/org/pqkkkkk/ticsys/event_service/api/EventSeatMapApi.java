package org.pqkkkkk.ticsys.event_service.api;

import org.pqkkkkk.ticsys.event_service.dto.DTO.EventSeatMapDTO;
import org.pqkkkkk.ticsys.event_service.dto.Response.ApiResponse;
import org.pqkkkkk.ticsys.event_service.dto.request.EventSeatMapRequest.CreateEventSeatMapRequest;
import org.pqkkkkk.ticsys.event_service.dto.request.EventSeatMapRequest.UpdateEventSeatMapRequest;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;
import org.pqkkkkk.ticsys.event_service.service.EventSeatMapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/event/seat-map")
public class EventSeatMapApi {
    private final EventSeatMapService eventSeatMapService;

    public EventSeatMapApi(EventSeatMapService eventSeatMapService) {
        this.eventSeatMapService = eventSeatMapService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventSeatMapDTO>> createEventSeatMap(@Valid @RequestBody CreateEventSeatMapRequest request) {
        EventSeatMap createdEventSeatMap = eventSeatMapService.createEventSeatMap(request.toEntity());

        EventSeatMapDTO dto = EventSeatMapDTO.from(createdEventSeatMap);

        ApiResponse<EventSeatMapDTO> response = new ApiResponse<>(dto,true, HttpStatus.CREATED.value(),
            "Event seat map created successfully",
            null
        );

        return ResponseEntity.ok(response);
    }
    @PutMapping
    public ResponseEntity<ApiResponse<EventSeatMapDTO>> updateEventSeatMap(@Valid @RequestBody UpdateEventSeatMapRequest request) {
        EventSeatMap updatedEventSeatMap = eventSeatMapService.updateEventSeatMap(request.toEntity());

        EventSeatMapDTO dto = EventSeatMapDTO.from(updatedEventSeatMap);

        ApiResponse<EventSeatMapDTO> response = new ApiResponse<>(dto,true, HttpStatus.OK.value(),
            "Event seat map updated successfully",
            null
        );

        return ResponseEntity.ok(response);
    }

}