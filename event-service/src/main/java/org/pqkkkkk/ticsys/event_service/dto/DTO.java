package org.pqkkkkk.ticsys.event_service.dto;

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

    ){
        public static EventMemberDTO from(EventMember eventMember){
            return new EventMemberDTO();
        }
    }
}
