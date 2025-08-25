package org.pqkkkkk.ticsys.event_service.entity.event_seat_map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_zone_custom_shape_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZoneCustomShape {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_custom_shape_id", nullable = false)
    Long zoneCustomShapeId;

    @Column(name = "x", nullable = false)
    Double x;
    
    @Column(name = "y", nullable = false)
    Double y;

    @ManyToOne
    @JoinColumn(name = "event_seat_map_zone_id", nullable = false)
    EventSeatMapZone eventSeatMapZone;
}
