package org.pqkkkkk.ticsys.promotion_service.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_promotion_service_voucher_promotion_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VoucherPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "promotion_name", nullable = false)
    String promotionName;

    @Column(name = "promotion_description")
    String promotionDescription;

    @Column(name = "voucher_type")
    @Enumerated(EnumType.STRING)
    VoucherType voucherType;

    // Only one of the following two fields will be used based on voucherType
    @Column(name = "voucher_value_by_percentage")
    Double voucherValueByPercentage;
    @Column(name = "voucher_value_by_amount")
    Double voucherValueByAmount;

    @Column(name = "voucher_promotion_type")
    @Enumerated(EnumType.STRING)
    VoucherPromotionType voucherPromotionType;

    // Equals to numberOfCodes * maxAppliedTicketCountPerCode for Bulk Code Promotion
    // Equals to maxAppliedTicketCountPerCode for Single Code Promotion
    @Column(name = "total_applied_ticket_count")
    Integer totalAppliedTicketCount;

    @Column(name = "current_applied_ticket_count")
    Integer currentAppliedTicketCount;

    @Column(name = "max_applied_ticket_count_per_code")
    Integer maxAppliedTicketCountPerCode;

    // Only for Bulk Code Promotion
    @Column(name = "number_of_codes")
    Integer numberOfCodes;

    @Column(name = "promotion_visibility")
    @Enumerated(EnumType.STRING)
    VoucherPromotionVisibility promotionVisibility;

    @Column(name = "start_date")
    LocalDateTime startDate;

    @Column(name = "end_date")
    LocalDateTime endDate;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "event_id", nullable = false)
    Long eventId;

    @Column(name = "event_date_id", nullable = true)
    Long eventDateId;

    @Column(name = "created_by", nullable = false)
    Long createdBy;

    // Only for Single Code Promotion
    @OneToOne(mappedBy = "voucherPromotion")
    SingleCodePromotionInfo singleCodePromotionInfo;

    // Only for Bulk Code Promotion
    @OneToMany(mappedBy = "voucherPromotion")
    List<BulkCodePromotionInfo> bulkCodePromotionInfos;
}
