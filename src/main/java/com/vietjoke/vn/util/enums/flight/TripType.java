package com.vietjoke.vn.util.enums.flight;

public enum TripType {
    ONEWAY("oneway"),
    ROUND_TRIP("round_trip");

    private final String value;

    TripType(String value) {
        this.value = new String(value);
    }

    public String getValue() {
        return value;
    }

    public static TripType fromValue(String value) {
        if (value != null) {
            for (TripType tripType : TripType.values()) {
                if (tripType.value.equalsIgnoreCase(value)) {
                    return tripType;
                }
            }
        }
        throw new IllegalArgumentException("No TripType with value " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}