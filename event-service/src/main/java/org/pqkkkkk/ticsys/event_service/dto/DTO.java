package org.pqkkkkk.ticsys.event_service.dto;

import org.pqkkkkk.ticsys.event_service.entity.Event;

public class DTO {
    public record EventDTO(

    ){
        public static EventDTO from(Event event){
            return new EventDTO();
        }
    }
}
