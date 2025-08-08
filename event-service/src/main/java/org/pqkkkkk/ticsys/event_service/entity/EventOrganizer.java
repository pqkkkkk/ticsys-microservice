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
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_event_organizer_table")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventOrganizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organizer_id")
    Long organizerId;
    @Column(name = "organizer_name", nullable = false, length = 100)
    String organizerName;
    @Column(name = "organizer_description", length = 500)
    String organizerDescription;
    @Column(name = "organizer_avatar_url")
    String organizerAvatarUrl;

    @OneToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;
}
