package org.pqkkkkk.ticsys.event_service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.pqkkkkk.ticsys.event_service.Contants.EventCategory;
import org.pqkkkkk.ticsys.event_service.Contants.EventLocationType;
import org.pqkkkkk.ticsys.event_service.Contants.EventSeatMapZoneShape;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.Contants.EventVisibility;
import org.pqkkkkk.ticsys.event_service.Contants.UserRoleInEvent;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.pqkkkkk.ticsys.event_service.entity.EventFAQ;
import org.pqkkkkk.ticsys.event_service.entity.EventMember;
import org.pqkkkkk.ticsys.event_service.entity.EventOfflineLocation;
import org.pqkkkkk.ticsys.event_service.entity.EventOnlineLocation;
import org.pqkkkkk.ticsys.event_service.entity.EventOrganizer;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMapZone;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.ZoneCustomShape;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.ZoneRectangleShape;

public class DTO {
    public record EventDTO(
        Long eventId,
        Long userId,
        String eventName,
        String eventDescription,
        String eventBanner,
        EventStatus eventStatus,
        EventCategory eventCategory,
        EventVisibility eventVisibility,
        EventLocationType eventLocationType,
        String messageToParticipants,
        LocalDateTime eventCreatedAt,
        LocalDateTime eventUpdatedAt,
        EventOrganizerDTO eventOrganizer,
        List<EventDateDTO> eventDates,
        EventOnlineLocationDTO eventOnlineLocation,
        EventOfflineLocationDTO eventOfflineLocation,
        List<EventFAQDTO> eventFAQs,
        List<EventMemberDTO> eventMembers
    ){
        public static EventDTO from(Event event){
            return new EventDTO(
                event.getEventId(),
                event.getUserId(),
                event.getEventName(),
                event.getEventDescription(),
                event.getEventBanner(),
                event.getEventStatus(),
                event.getEventCategory(),
                event.getEventVisibility(),
                event.getEventLocationType(),
                event.getMessageToParticipants(),
                event.getEventCreatedAt(),
                event.getEventUpdatedAt(),
                event.getEventOrganizer() != null ? EventOrganizerDTO.from(event.getEventOrganizer()) : null,
                event.getEventDates() != null ? event.getEventDates().stream().map(EventDateDTO::from).collect(Collectors.toList()) : null,
                event.getEventOnlineLocation() != null ? EventOnlineLocationDTO.from(event.getEventOnlineLocation()) : null,
                event.getEventOfflineLocation() != null ? EventOfflineLocationDTO.from(event.getEventOfflineLocation()) : null,
                event.getEventFAQs() != null ? event.getEventFAQs().stream().map(EventFAQDTO::from).collect(Collectors.toList()) : null,
                event.getEventMembers() != null ? event.getEventMembers().stream().map(EventMemberDTO::from).collect(Collectors.toList()) : null
            );
        }
    }

    public record EventOrganizerDTO(
        Long organizerId,
        String organizerName,
        String organizerDescription,
        String organizerAvatarUrl
    ){
        public static EventOrganizerDTO from(EventOrganizer organizer){
            return new EventOrganizerDTO(
                organizer.getOrganizerId(),
                organizer.getOrganizerName(),
                organizer.getOrganizerDescription(),
                organizer.getOrganizerAvatarUrl()
            );
        }
    }

    public record EventDateDTO(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        List<TicketDTO> tickets,
        EventSeatMapDTO eventSeatMap
    ){
        public static EventDateDTO from(EventDate eventDate){
            return new EventDateDTO(
                eventDate.getId(),
                eventDate.getDate(),
                eventDate.getStartTime(),
                eventDate.getEndTime(),
                eventDate.getTickets() != null ? eventDate.getTickets().stream().map(TicketDTO::from).collect(Collectors.toList()) : null,
                eventDate.getEventSeatMap() != null ? EventSeatMapDTO.from(eventDate.getEventSeatMap()) : null
            );
        }
    }

    public record EventOnlineLocationDTO(
        Long eventOnlineLocationId,
        String meetingLink,
        String meetingId,
        String password
    ){
        public static EventOnlineLocationDTO from(EventOnlineLocation location){
            return new EventOnlineLocationDTO(
                location.getEventOnlineLocationId(),
                location.getMeetingLink(),
                location.getMeetingId(),
                location.getPassword()
            );
        }
    }

    public record EventOfflineLocationDTO(
        Long eventOfflineLocationId,
        String province,
        String ward,
        String street,
        String venueName
    ){
        public static EventOfflineLocationDTO from(EventOfflineLocation location){
            return new EventOfflineLocationDTO(
                location.getEventOfflineLocationId(),
                location.getProvince(),
                location.getWard(),
                location.getStreet(),
                location.getVenueName()
            );
        }
    }

    public record EventFAQDTO(
        Long eventFAQId,
        String question,
        String answer
    ){
        public static EventFAQDTO from(EventFAQ faq){
            return new EventFAQDTO(
                faq.getEventFAQId(),
                faq.getQuestion(),
                faq.getAnswer()
            );
        }
    }

    public record EventMemberDTO(
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

    public record TicketDTO(
        Long ticketId,
        String ticketName,
        String ticketService,
        Double ticketPrice,
        Integer ticketTotalQuantity,
        Integer ticketStockQuantity,
        Integer maxQtyPerOrder,
        LocalDateTime soldStartTime,
        LocalDateTime soldEndTime,
        List<EventSeatMapZoneDTO> eventSeatMapZones
    ){
        public static TicketDTO from(Ticket ticket){
            return new TicketDTO(
                ticket.getTicketId(),
                ticket.getTicketName(),
                ticket.getTicketService(),
                ticket.getTicketPrice(),
                ticket.getTicketTotalQuantity(),
                ticket.getTicketStockQuantity(),
                ticket.getMaxQtyPerOrder(),
                ticket.getSoldStartTime(),
                ticket.getSoldEndTime(),
                ticket.getEventSeatMapZones() != null ? 
                    ticket.getEventSeatMapZones().stream().map(EventSeatMapZoneDTO::from).collect(Collectors.toList()) : null
            );
        }
    }

    public record EventSeatMapDTO(
        Long eventSeatMapId,
        List<EventSeatMapZoneDTO> eventSeatMapZones
    ){
        public static EventSeatMapDTO from(EventSeatMap eventSeatMap){
            return new EventSeatMapDTO(
                eventSeatMap.getEventSeatMapId(),
                eventSeatMap.getEventSeatMapZones() != null ? 
                    eventSeatMap.getEventSeatMapZones().stream().map(EventSeatMapZoneDTO::from).collect(Collectors.toList()) : null
            );
        }
    }

    public record EventSeatMapZoneDTO(
        Long eventSeatMapZoneId,
        Integer ticketQuantity,
        String zoneName,
        EventSeatMapZoneShape zoneShape,
        String fill,
        List<ZoneCustomShapeDTO> zoneCustomShapes,
        ZoneRectangleShapeDTO zoneRectangleShape
    ){
        public static EventSeatMapZoneDTO from(EventSeatMapZone zone){
            return new EventSeatMapZoneDTO(
                zone.getEventSeatMapZoneId(),
                zone.getTicketQuantity(),
                zone.getZoneName(),
                zone.getZoneShape(),
                zone.getFill(),
                zone.getZoneCustomShapes() != null ? 
                    zone.getZoneCustomShapes().stream().map(ZoneCustomShapeDTO::from).collect(Collectors.toList()) : null,
                zone.getZoneRectangleShape() != null ? ZoneRectangleShapeDTO.from(zone.getZoneRectangleShape()) : null
            );
        }
    }

    public record ZoneCustomShapeDTO(
        Long zoneCustomShapeId,
        Double x,
        Double y
    ){
        public static ZoneCustomShapeDTO from(ZoneCustomShape shape){
            return new ZoneCustomShapeDTO(
                shape.getZoneCustomShapeId(),
                shape.getX(),
                shape.getY()
            );
        }
    }

    public record ZoneRectangleShapeDTO(
        Long zoneRectangleShapeId,
        Double x,
        Double y,
        Double width,
        Double height,
        Double rotation
    ){
        public static ZoneRectangleShapeDTO from(ZoneRectangleShape shape){
            return new ZoneRectangleShapeDTO(
                shape.getZoneRectangleShapeId(),
                shape.getX(),
                shape.getY(),
                shape.getWidth(),
                shape.getHeight(),
                shape.getRotation()
            );
        }
    }
}
