package org.pqkkkkk.ticsys.promotion_service.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyPromotionInfo {
    Long promotionId; // The promotion ID to be applied
    Long userId; // The ID of the user applying the promotion
    Long eventId; // The ID of the event for which the promotion is applied
    Long eventDateId; // The ID of the specific date of the event
    Double originalPrice; // The original price before applying the promotion
    String voucherCode; // The voucher code if applicable
    String voucherType; // The type of voucher (e.g., SINGLE_CODE, BULK_CODE)
    Integer ticketQuantity; // The quantity of tickets or items
}
