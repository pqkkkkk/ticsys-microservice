package org.pqkkkkk.ticsys.promotion_service.domain.entity;

import jakarta.persistence.Column;
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
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ticsys_promotion_service_single_code_promotion_info_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SingleCodePromotionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scpi_id")
    Long scpiId;

    @Column(name = "code_value",nullable = false, unique = true, length = 12)
    String codeValue;

    @Column(name = "max_applied_order_per_user")
    Integer maxAppliedOrderPerUser;

    @Column(name = "min_ticket_quantity_per_order_to_apply")
    Integer minTicketQuantityPerOrderToApply;

    @Column(name = "max_ticket_quantity_per_order_to_apply")
    Integer maxTicketQuantityPerOrderToApply;

    @OneToOne
    @JoinColumn(name = "voucher_promotion_id")
    VoucherPromotion voucherPromotion;
}
