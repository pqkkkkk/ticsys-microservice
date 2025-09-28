package org.pqkkkkk.ticsys.promotion_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_promotion_service_bulk_code_promotion_info_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BulkCodePromotionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bcpi_id")
    Long bcpiId;
    
    @Column(name = "code_prefix")
    String codePrefix;

    @Column(name = "code_value")
    String codeValue;

    @Column(name = "is_used")
    Boolean isUsed;

    // User ID who used this code; null if not used
    @Column(name = "used_by")
    Long usedBy;

    @ManyToOne
    @JoinColumn(name = "voucher_promotion_id")
    VoucherPromotion voucherPromotion;
}
