package org.pqkkkkk.ticsys.event_service.entity.event_seat_map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_zone_rectangle_shape_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZoneRectangleShape {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_rectangle_shape_id", nullable = false)
    Long zoneRectangleShapeId;

    @Column(name = "x", nullable = false)
    Double x;

    @Column(name = "y", nullable = false)
    Double y;

    @Column(name = "width", nullable = false)
    Double width;

    @Column(name = "height", nullable = false)
    Double height;

    @Column(name = "rotation", nullable = false)
    Double rotation; // in degrees

    @OneToOne
    @JoinColumn(name = "event_seat_map_zone_id", nullable = false)
    EventSeatMapZone eventSeatMapZone;
}
