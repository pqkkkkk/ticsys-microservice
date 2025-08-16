package org.pqkkkkk.ticsys.event_service.api;

import org.pqkkkkk.ticsys.event_service.dto.DTO.EventDTO;
import org.pqkkkkk.ticsys.event_service.dto.Response.ApiResponse;
import org.pqkkkkk.ticsys.event_service.dto.filter_object.EventFilter;
import org.pqkkkkk.ticsys.event_service.dto.request.EventRequest.CreateEventRequest;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.service.EventQueryService;
import org.pqkkkkk.ticsys.event_service.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/event")
public class EventApi {
    private final EventService eventService;
    private final EventQueryService eventQueryService;

    public EventApi(EventService eventService, EventQueryService eventQueryService) {
        this.eventService = eventService;
        this.eventQueryService = eventQueryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventDTO>> createEvent(@Valid @RequestBody CreateEventRequest request) {
        Event event = eventService.createEvent(request.toEntity(), request.currentStep());
        
        EventDTO eventDTO = EventDTO.from(event);

        ApiResponse<EventDTO> response = new ApiResponse<>(eventDTO,true, HttpStatus.CREATED.value(),
            "Event created successfully",
            null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping(value = "/{eventId}/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadBanner(
        @PathVariable ("eventId") Long eventId,
        @RequestPart("banner") MultipartFile banner) {

        String fileId = eventService.uploadBanner(eventId, banner);

        ApiResponse<String> response = new ApiResponse<>(fileId, true, HttpStatus.OK.value(),
            "Banner uploaded successfully",
            null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Event>>> getEvents(@Valid @ModelAttribute EventFilter filter){

        Page<Event> events = eventQueryService.getEvents(filter);

        ApiResponse<Page<Event>> response = new ApiResponse<>(events, true, HttpStatus.OK.value(),
            "Events retrieved successfully",
            null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventDTO>> getEventById(@PathVariable Long eventId) {

        Event event = eventQueryService.getEventById(eventId);

        EventDTO eventDTO = EventDTO.from(event);

        ApiResponse<EventDTO> response = new ApiResponse<>(eventDTO, true, HttpStatus.OK.value(),
            "Event retrieved successfully",
            null
        );

        return ResponseEntity.ok(response);
    }

}