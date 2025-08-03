package org.pqkkkkk.ticsys.event_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_event_offline_location_table")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventOfflineLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_offline_location_id")
    Long eventOfflineLocaltionId;

    String province;
    String ward;
    String street;
    String venueName;

    @OneToOne
    Event event;
}
