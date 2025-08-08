package org.pqkkkkk.ticsys.event_service;

public class Contants {
    public enum EventStatus {
        DRAFT,
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
    public enum EventCreationStep {
        BASIC_INFO (1),
        TIME_AND_TICKET (2),
        SETTINGS (3);

        private Integer step;

        private EventCreationStep(Integer step){
            this.step = step;
        }

        public Integer getStep(){
            return step;
        }
        public static EventCreationStep fromStep(Integer step) {
            for (EventCreationStep eventStep : EventCreationStep.values()) {
                if (eventStep.getStep().equals(step)) {
                    return eventStep;
                }
            }
            throw new IllegalArgumentException("Invalid step: " + step);
        }
    }
}
