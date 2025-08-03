package org.pqkkkkk.ticsys.event_service.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.pqkkkkk.ticsys.event_service.entity.event_seat_map.EventSeatMapZone;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_event_date_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "date", nullable = false)
    LocalDate date;
    @Column(name = "start_time", nullable = false)
    LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @OneToMany(mappedBy = "eventDate", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Ticket> tickets;

    @OneToMany(mappedBy = "eventDate", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<EventSeatMapZone> eventSeatMapZones;
}
