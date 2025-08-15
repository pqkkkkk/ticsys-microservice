package org.pqkkkkk.ticsys.event_service.dto.request;

import org.pqkkkkk.ticsys.event_service.Contants.UserRoleInEvent;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;

import jakarta.validation.constraints.NotNull;

public class EventMemberRequest {
    public record CreateEventMemberRequest(
        @NotNull(message = "User ID cannot be null")
        Long userId,
        @NotNull(message = "Event ID cannot be null")
        Long eventId,
        @NotNull(message = "Role cannot be null")
        UserRoleInEvent role
    ){
        public EventMember toEntity() {
            EventMember eventMember = new EventMember();
            eventMember.setUserId(userId);
            eventMember.setEvent(Event.builder().eventId(eventId).build());
            eventMember.setUserRoleInEvent(role);
            return eventMember;
        }
    }
}
