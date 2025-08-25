package org.pqkkkkk.ticsys.event_service.dto.request;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.Contants.EventSeatMapZoneShape;
import org.pqkkkkk.ticsys.event_service.entity.EventDate;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMap;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMapZone;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.ZoneCustomShape;
import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.ZoneRectangleShape;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class EventSeatMapRequest {
    public record CreateEventSeatMapRequest(
        @NotNull(message = "Event date ID must not be null")
        Long eventDateId,
        List<CreateEventSeatMapZoneRequest> zones
    ){
        public EventSeatMap toEntity(){
            EventSeatMap eventSeatMap = EventSeatMap.builder()
            .eventDate(EventDate.builder().id(eventDateId).build())
            .build();

            eventSeatMap.setEventSeatMapZones(zones.stream()
                .map(createEventSeatMapZone -> {
                    EventSeatMapZone zone = createEventSeatMapZone.toEntity();
                    zone.setEventSeatMap(eventSeatMap);
                    return zone;
                })
                .toList());

            return eventSeatMap;
        }
    }

    public record CreateEventSeatMapZoneRequest(
        @NotNull(message = "Zone name must not be null")
        @NotBlank(message = "Zone name must not be blank")
        String zoneName,
        EventSeatMapZoneShape zoneShape,
        @NotNull(message = "Fill must not be null")
        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Fill must be a valid hex color")
        String fill,
        @NotNull(message = "Ticket ID must not be null")
        Long ticketId,
        @NotNull(message = "Ticket quantity must not be null")
        @Min(value = 1, message = "Ticket quantity must be at least 1")
        Integer ticketQuantity,
        CreateZoneRectangleRequest rectangle,
        List<CreateZoneCustomRequest> customZones
    ){
        public EventSeatMapZone toEntity(){
            EventSeatMapZone zone = EventSeatMapZone.builder()
                                .zoneName(zoneName)
                                .zoneShape(zoneShape)
                                .fill(fill)
                                .ticket(Ticket.builder().ticketId(ticketId).build())
                                .ticketQuantity(ticketQuantity)
                                .build();

            ZoneRectangleShape rectangleShape = rectangle.toEntity();
            rectangleShape.setEventSeatMapZone(zone);
            zone.setZoneRectangleShape(rectangleShape);

            List<ZoneCustomShape> customShapes = customZones.stream()
                .map(customZoneRequest -> {
                    ZoneCustomShape shape = customZoneRequest.toEntity();
                    shape.setEventSeatMapZone(zone);
                    return shape;
                })
                .toList();
            zone.setZoneCustomShapes(customShapes);

            return zone;
        }
    }

    public record CreateZoneRectangleRequest(
        @NotNull(message = "X coordinate must not be null")
        Double x,
        @NotNull(message = "Y coordinate must not be null")
        Double y,
        @NotNull(message = "Width must not be null")
        Double width,
        @NotNull(message = "Height must not be null")
        Double height,
        Double rotation
    ){
        public ZoneRectangleShape toEntity(){
            ZoneRectangleShape shape = new ZoneRectangleShape();
            shape.setX(x);
            shape.setY(y);
            shape.setWidth(width);
            shape.setHeight(height);
            shape.setRotation(rotation == null ? 0 : rotation);
            return shape;
        }
    }

    public record CreateZoneCustomRequest(
        @NotNull(message = "X coordinate must not be null")
        Double x,
        @NotNull(message = "Y coordinate must not be null")
        Double y
    ){
        public ZoneCustomShape toEntity(){
            ZoneCustomShape shape = new ZoneCustomShape();
            shape.setX(x);
            shape.setY(y);
            return shape;
        }
    }

    public record UpdateEventSeatMapRequest(
        @NotNull(message = "Event seat map ID must not be null")
        Long eventSeatMapId,
        @NotNull(message = "Event date ID must not be null")
        Long eventDateId,
        List<CreateEventSeatMapZoneRequest> zones
    ){
        public EventSeatMap toEntity(){
            EventSeatMap eventSeatMap = EventSeatMap.builder()
            .eventSeatMapId(eventSeatMapId)
            .eventDate(EventDate.builder().id(eventDateId).build())
            .build();

            eventSeatMap.setEventSeatMapZones(zones.stream()
                .map(createEventSeatMapZone -> {
                    EventSeatMapZone zone = createEventSeatMapZone.toEntity();
                    zone.setEventSeatMap(eventSeatMap);
                    return zone;
                })
                .toList());

            return eventSeatMap;
        }
    }

    public record UpdateEventSeatMapZoneRequest(
        @NotNull(message = "Event seat map zone ID must not be null")
        Long eventSeatMapZoneId,
        @NotNull(message = "Zone name must not be null")
        @NotBlank(message = "Zone name must not be blank")
        String zoneName,
        EventSeatMapZoneShape zoneShape,
        @NotNull(message = "Fill must not be null")
        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Fill must be a valid hex color")
        String fill,
        @NotNull(message = "Ticket ID must not be null")
        Long ticketId,
        @NotNull(message = "Ticket quantity must not be null")
        @Min(value = 1, message = "Ticket quantity must be at least 1")
        Integer ticketQuantity,
        CreateZoneRectangleRequest rectangle,
        List<CreateZoneCustomRequest> customZones
    ){
        public EventSeatMapZone toEntity(){
            EventSeatMapZone zone = EventSeatMapZone.builder()
                                .eventSeatMapZoneId(eventSeatMapZoneId)
                                .zoneName(zoneName)
                                .zoneShape(zoneShape)
                                .fill(fill)
                                .ticket(Ticket.builder().ticketId(ticketId).build())
                                .ticketQuantity(ticketQuantity)
                                .build();

            ZoneRectangleShape rectangleShape = rectangle.toEntity();
            rectangleShape.setEventSeatMapZone(zone);
            zone.setZoneRectangleShape(rectangleShape);

            List<ZoneCustomShape> customShapes = customZones.stream()
                .map(customZoneRequest -> {
                    ZoneCustomShape shape = customZoneRequest.toEntity();
                    shape.setEventSeatMapZone(zone);
                    return shape;
                })
                .toList();
            zone.setZoneCustomShapes(customShapes);

            return zone;
        }
    }

    public record UpdateZoneRectangleRequest(
        @NotNull(message = "Zone rectangle ID must not be null")
        Long zoneRectangleId,
        @NotNull(message = "X coordinate must not be null")
        Double x,
        @NotNull(message = "Y coordinate must not be null")
        Double y,
        @NotNull(message = "Width must not be null")
        Double width,
        @NotNull(message = "Height must not be null")
        Double height,
        Double rotation
    ){
        public ZoneRectangleShape toEntity(){
            ZoneRectangleShape shape = new ZoneRectangleShape();
            shape.setZoneRectangleShapeId(zoneRectangleId);
            shape.setX(x);
            shape.setY(y);
            shape.setWidth(width);
            shape.setHeight(height);
            shape.setRotation(rotation == null ? 0 : rotation);
            return shape;
        }
    }

    public record UpdateZoneCustomRequest(
        @NotNull(message = "Zone custom ID must not be null")
        Long zoneCustomId,
        @NotNull(message = "X coordinate must not be null")
        Double x,
        @NotNull(message = "Y coordinate must not be null")
        Double y
    ){
        public ZoneCustomShape toEntity(){
            ZoneCustomShape shape = new ZoneCustomShape();
            shape.setZoneCustomShapeId(zoneCustomId);
            shape.setX(x);
            shape.setY(y);
            return shape;
        }
    }
}
