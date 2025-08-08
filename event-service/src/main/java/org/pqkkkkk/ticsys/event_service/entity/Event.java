package org.pqkkkkk.ticsys.event_service.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.pqkkkkk.ticsys.event_service.Contants.EventCategory;
import org.pqkkkkk.ticsys.event_service.Contants.EventLocationType;
import org.pqkkkkk.ticsys.event_service.Contants.EventStatus;
import org.pqkkkkk.ticsys.event_service.Contants.EventVisibility;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "ticsys_event_service_event_table")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long eventId;

    @Column(name = "user_id", nullable =  false)
    Long userId;

    @Column(name = "event_name", nullable = false, length = 100)
    String eventName;

    @Column(name = "event_description", length = 500)
    String eventDescription;

    @Column(name = "event_banner", nullable = true)
    String eventBanner;

    @Column(name = "event_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    EventStatus eventStatus = EventStatus.DRAFT;

    @Column(name = "event_category")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    EventCategory eventCategory = EventCategory.OTHER;

    @Column(name = "event_visibility")
    @Enumerated(EnumType.STRING)
    EventVisibility eventVisibility;

    @Column(name = "event_location_type")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    EventLocationType eventLocationType = EventLocationType.OFFLINE;

    @Column(name = "message_to_participants", length = 500)
    String messageToParticipants;

    @Column(name = "event_created_at")
    @CreationTimestamp
    LocalDateTime eventCreatedAt;

    @Column(name = "event_updated_at")
    @UpdateTimestamp
    LocalDateTime eventUpdatedAt;

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    EventOrganizer eventOrganizer;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventDate> eventDates;

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    EventOnlineLocation eventOnlineLocation;

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    EventOfflineLocation eventOfflineLocation;
    
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventFAQ> eventFAQs;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventMember> eventMembers;
}
