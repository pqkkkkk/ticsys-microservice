package org.pqkkkkk.ticsys.event_service.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.pqkkkkk.ticsys.event_service.Contants.EventCategory;
import org.pqkkkkk.ticsys.event_service.Contants.EventLocationType;
import org.pqkkkkk.ticsys.event_service.Contants.EventVisibility;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.pqkkkkk.ticsys.event_service.entity.EventFAQ;
import org.pqkkkkk.ticsys.event_service.entity.EventOfflineLocation;
import org.pqkkkkk.ticsys.event_service.entity.EventOnlineLocation;
import org.pqkkkkk.ticsys.event_service.entity.EventOrganizer;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class EventRequest {
    public record CreateEventRequest(
        Long eventId,
        @Min(value = 1, message = "Current step must be at least 1")
        @Max(value = 3, message = "Current step must be less than 3")
        Integer currentStep,
        @NotNull(message =  "User ID cannot be null")
        Long userId,
        String name,
        String description,
        EventCategory category,
        EventVisibility visibility,
        EventLocationType locationType,
        String messageToParticipants,
        CreateEventOrganizerRequest organizerRequest,
        List<CreateEventDateRequest> dates,
        List<CreateEventFAQRequest> faqs,
        CreateEventOfflineLocationRequest offlineLocation,
        CreateEventOnlineLocationRequest onlineLocation
    ){
        public Event toEntity(){
            Event event = Event.builder()
                .eventId(eventId)
                .userId(userId)
                .eventName(name)
                .eventDescription(description)
                .eventCategory(category)
                .eventVisibility(visibility)
                .eventLocationType(locationType)
                .messageToParticipants(messageToParticipants)
                .eventOrganizer(organizerRequest != null ?  organizerRequest.toEntity() : null)
                .eventDates(dates != null ? dates.stream().map(CreateEventDateRequest::toEntity).collect(Collectors.toList()) : null)
                .eventFAQs(faqs != null ? faqs.stream().map(CreateEventFAQRequest::toEntity).collect(Collectors.toList()) : null)
                .eventOfflineLocation(offlineLocation != null ? offlineLocation.toEntity() : null)
                .eventOnlineLocation(onlineLocation != null ? onlineLocation.toEntity() : null)
                .build();

            if(event.getEventOnlineLocation() != null) {
                event.getEventOnlineLocation().setEvent(event);
            }
            if(event.getEventOfflineLocation() != null) {
                event.getEventOfflineLocation().setEvent(event);
            }
            if(event.getEventOrganizer() != null) {
                event.getEventOrganizer().setEvent(event);
            }
            if(event.getEventDates() != null) {
                event.getEventDates().forEach(date -> date.setEvent(event));
            }
            if(event.getEventFAQs() != null) {
                event.getEventFAQs().forEach(faq -> faq.setEvent(event));
            }

            return event;
        }
    }
    public record CreateEventOrganizerRequest(
        @NotBlank(message = "Organizer name cannot be blank")
        String organizerName,
        @NotBlank(message = "Organizer description cannot be blank")
        String organizerDescription
    ){
        public EventOrganizer toEntity() {
            return EventOrganizer.builder()
                .organizerName(organizerName)
                .organizerDescription(organizerDescription)
                .build();
        }
    }
    public record CreateEventFAQRequest(
        @NotBlank(message =  "Question cannot be blank")
        String question,
        @NotBlank(message = "Answer cannot be blank")
        String answer
    ){
        public EventFAQ toEntity(){
            return EventFAQ.builder()
                .question(question)
                .answer(answer)
                .build();
        }
    }
    public record CreateEventOfflineLocationRequest(
        @NotBlank(message = "Province cannot be blank")
        String province,
        @NotBlank(message = "Ward cannot be blank")
        String ward,
        @NotBlank(message = "Street cannot be blank")
        String street,
        @NotBlank(message = "Venue name cannot be blank")
        String venueName
    ) {
        public EventOfflineLocation toEntity() {
            return EventOfflineLocation.builder()
                .province(province)
                .ward(ward)
                .street(street)
                .venueName(venueName)
                .build();
        }
    }
    public record CreateEventOnlineLocationRequest(
        @NotBlank(message = "Link cannot be blank")
        String meetingLink,
        @NotBlank(message = "Meeting ID cannot be blank")
        String meetingId,
        String password
    ) {
        public EventOnlineLocation toEntity() {
            return EventOnlineLocation.builder()
                .meetingLink(meetingLink)
                .meetingId(meetingId)
                .password(password)
                .build();
        }
    }
    public record CreateEventDateRequest(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        @NotEmpty(message = "At least one ticket is required")
        List<CreateTicketRequest> tickets
    ){
        public EventDate toEntity() {
            EventDate eventDate = EventDate.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .tickets(tickets != null ? tickets.stream().map(CreateTicketRequest::toEntity).collect(Collectors.toList()) : null)
                .build();

            if(eventDate.getTickets() != null){
                eventDate.getTickets().forEach(ticket -> ticket.setEventDate(eventDate));
            }

            return eventDate;
        }
    }
    public record CreateTicketRequest(
        @NotBlank(message = "Ticket name cannot be blank")
        String name,
        @NotBlank(message = "Ticket description cannot be blank")
        String service,
        @NotBlank(message = "Ticket price cannot be blank")
        Double price,
        @Min(value = 1, message = "Total quantity must be at least 1")
        Integer totalQuantity,
        Integer maxQtyPerOrder,
        LocalDateTime soldStartTime,
        LocalDateTime soldEndTime
    ){
        public Ticket toEntity() {
            return Ticket.builder()
                .ticketName(name)
                .ticketService(service)
                .ticketPrice(price)
                .ticketTotalQuantity(totalQuantity)
                .ticketStockQuantity(totalQuantity) // Initially, stock quantity is the same as total quantity
                .maxQtyPerOrder(maxQtyPerOrder)
                .soldStartTime(soldStartTime)
                .soldEndTime(soldEndTime)
                .build();
        }
    }
}
