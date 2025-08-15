package org.pqkkkkk.ticsys.event_service.api;

import org.pqkkkkk.ticsys.event_service.dto.DTO.EventMemberDTO;
import org.pqkkkkk.ticsys.event_service.dto.Response.ApiResponse;
import org.pqkkkkk.ticsys.event_service.dto.request.EventMemberRequest.CreateEventMemberRequest;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.pqkkkkk.ticsys.event_service.service.EventMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/event/member")
public class EventMemberApi {
    private final EventMemberService eventMemberService;

    public EventMemberApi(EventMemberService eventMemberService) {
        this.eventMemberService = eventMemberService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventMemberDTO>> addEventMember(@Valid @RequestBody CreateEventMemberRequest request) {
        EventMember eventMember = eventMemberService.addEventMember(request.toEntity());

        EventMemberDTO dto = EventMemberDTO.from(eventMember);

        ApiResponse<EventMemberDTO> response = new ApiResponse<>(dto, true, HttpStatus.CREATED.value(),
                            "Event member added successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventMemberDTO>> updateEventMember(@PathVariable Long id, @Valid @RequestBody CreateEventMemberRequest request) {
        EventMember eventMember = eventMemberService.updateEventMember(request.toEntity());

        EventMemberDTO dto = EventMemberDTO.from(eventMember);

        ApiResponse<EventMemberDTO> response = new ApiResponse<>(dto, true, HttpStatus.OK.value(),
                            "Event member updated successfully", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEventMember(@PathVariable Long id) {
        eventMemberService.removeEventMember(id);

        ApiResponse<Void> response = new ApiResponse<>(null, true, HttpStatus.NO_CONTENT.value(),
                            "Event member deleted successfully", null);
                            
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
