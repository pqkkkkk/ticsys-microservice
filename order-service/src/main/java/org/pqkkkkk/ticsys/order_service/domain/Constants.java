package org.pqkkkkk.ticsys.order_service.domain;

public class Constants {
    public static final Integer ORDER_EXPIRATION_MINUTES = 15;
    
    public enum OrderStatus {
        PENDING,
        PROCESSING,
        PAID,
        CANCELLED,
        EXPIRED
    }
}
