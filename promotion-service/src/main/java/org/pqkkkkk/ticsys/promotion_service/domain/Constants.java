package org.pqkkkkk.ticsys.promotion_service.domain;

public class Constants {
    public static final Integer SUBFIX_OF_BULK_CODE_LENGTH = 8;
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";

    public enum VoucherPromotionType {
        SINGLE_CODE,
        BULK_CODE
    }
    public enum VoucherType {
        BY_AMOUNT,
        BY_PERCENTAGE
    }
    public enum VoucherPromotionVisibility {
        PUBLIC,
        PRIVATE
    }
    public enum SortDirection {
        ASC,
        DESC
    }
}
