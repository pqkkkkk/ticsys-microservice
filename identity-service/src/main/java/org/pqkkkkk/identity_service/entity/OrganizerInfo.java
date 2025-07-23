package org.pqkkkkk.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticsys_identity_service_organizer_info_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organizerInfoId;
    String name;
    String description;
    
    @OneToOne
    @JoinColumn(name = "organizer_id")
    User user;
}
