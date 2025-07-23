package org.pqkkkkk.identity_service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Constants {
    public enum RoleEnum{
        USER,
        ORGANIZER,
        ADMIN
    }
    public enum GenderEnum{
        MALE,
        FEMALE,
        OTHER;

        @JsonValue
        public String toValue() {
            return this.name();
        }

        @JsonCreator
        public static GenderEnum fromValue(String value) {
            if (value == null) {
                return null;
            }
            
            String upperValue = value.toUpperCase();
            
            for (GenderEnum gender : GenderEnum.values()) {
                if (gender.name().equals(upperValue)) {
                    return gender;
                }
            }
            
            throw new IllegalArgumentException("Invalid gender value: " + value + 
                ". Accepted values are: MALE, FEMALE, OTHER");
        }
    }
}
