package org.pqkkkkk.ticsys.event_service.dto;

import org.pqkkkkk.ticsys.event_service.Contants.UserRoleInEvent;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;

public class DTO {
    public record EventDTO(

    ){
        public static EventDTO from(Event event){
            return new EventDTO();
        }
    }
    public record EventMemberDTO (
        Long id,
        Long userId,
        Long eventId,
        UserRoleInEvent role
    ){
        public static EventMemberDTO from(EventMember eventMember){
            return new EventMemberDTO(
                eventMember.getEventMemberId(),
                eventMember.getUserId(),
                eventMember.getEvent().getEventId(),
                eventMember.getUserRoleInEvent()
            );
        }
    }
}
