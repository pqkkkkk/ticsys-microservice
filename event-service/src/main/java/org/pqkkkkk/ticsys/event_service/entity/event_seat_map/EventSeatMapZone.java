package org.pqkkkkk.ticsys.event_service.entity.event_seat_map;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.Contants.EventSeatMapZoneShape;
import org.pqkkkkk.ticsys.event_service.entity.Ticket;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_event_seat_map_zone_table")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventSeatMapZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_seat_map_zone_id", nullable = false)
    Long eventSeatMapZoneId;

    @Column(name = "ticket_quantity", nullable = false)
    @Min(value = 1, message = "Ticket quantity must be at least 1")
    Integer ticketQuantity;

    @Column(name = "zone_name", nullable = false, length = 30)
    String zoneName;

    @Column(name = "zone_shape", nullable = false)
    @Enumerated(EnumType.STRING)
    EventSeatMapZoneShape zoneShape;

    @Column(name = "fill",length = 7, nullable = false ) // Hex color code
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Fill must be a valid hex color")
    String fill;

    @ManyToOne
    @JoinColumn(name = "event_seat_map", nullable = false)
    EventSeatMap eventSeatMap;

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    Ticket ticket;

    @OneToMany(mappedBy = "eventSeatMapZone", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<ZoneCustomShape> zoneCustomShapes;

    @OneToOne(mappedBy = "eventSeatMapZone", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    ZoneRectangleShape zoneRectangleShape;
}
