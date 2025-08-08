package org.pqkkkkk.ticsys.event_service.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_event_service_event_faq_table")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_faq_id")
    Long eventFAQId;

    @Column(name = "question", length = 100, nullable = false)
    String question;

    @Column(name = "answer", length = 300, nullable = false)
    String answer;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;
}
