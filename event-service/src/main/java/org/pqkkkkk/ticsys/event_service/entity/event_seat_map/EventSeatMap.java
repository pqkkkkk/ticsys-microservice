package org.pqkkkkk.ticsys.event_service.entity.event_seat_map;

import java.util.List;

import org.pqkkkkk.ticsys.event_service.entity.EventDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_event_seat_map_table")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventSeatMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_seat_map_id")
    Long eventSeatMapId;

    @OneToMany(mappedBy = "eventSeatMap", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<EventSeatMapZone> eventSeatMapZones;

    @OneToOne
    @JoinColumn(name = "event_date_id", nullable = false)
    EventDate eventDate;
}
