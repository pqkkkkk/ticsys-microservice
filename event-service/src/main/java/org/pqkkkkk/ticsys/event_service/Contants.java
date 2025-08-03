package org.pqkkkkk.ticsys.event_service;

public class Contants {
    public enum EventStatus {
        PENDING,
        REJECTED,
        ON_GOING,
        COMPLETED,
        CANCELLED
    }
    public enum EventCategory{
        MUSIC,
        SPORTS,
        ART,
        OTHER
    }
    public enum EventLocationType {
        ONLINE,
        OFFLINE
    }
    public enum EventVisibility {
        PUBLIC,
        PRIVATE
    }
    public enum UserRoleInEvent{
        ADMIN,
        MEMBER
    }
    public enum EventSeatMapZoneShape {
        RECTANGLE,
        CUSTOM,
    }
}
