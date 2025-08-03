package org.pqkkkkk.ticsys.event_service.entity;

import org.pqkkkkk.ticsys.event_service.Contants.UserRoleInEvent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_event_member_table")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_member_id")
    Long eventMemberId;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "user_role_in_event")
    UserRoleInEvent userRoleInEvent;

    @ManyToOne
    Event event;
}
