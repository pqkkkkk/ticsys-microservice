package org.pqkkkkk.ticsys.event_service.entity;

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
@Table(name = "ticsys_event_service_event_online_location_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventOnlineLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_online_location_id")
    Long eventOnlineLocationId;

    @Column(name = "meeting_link", nullable = false)
    String meetingLink;
    @Column(name = "meeting_id", nullable = false)
    String meetingId;
    @Column(name = "password")
    String password;

    @OneToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;
}
