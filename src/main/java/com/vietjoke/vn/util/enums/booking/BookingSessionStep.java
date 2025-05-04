package com.vietjoke.vn.util.enums.booking;

public enum BookingSessionStep {
    SEARCH,            // Search for flights
    SELECT_FLIGHT,     // Select a flight
    INPUT_PASSENGERS,  // Enter passenger information
    SELECT_SERVICE,    // Select services: seats, addons
    PREVIEW,            // Review booking
    PAYMENT;           // Make payment

    public BookingSessionStep next() {
        return switch (this) {
            case SEARCH -> SELECT_FLIGHT;
            case SELECT_FLIGHT -> INPUT_PASSENGERS;
            case INPUT_PASSENGERS -> SELECT_SERVICE;
            case SELECT_SERVICE -> PREVIEW;
            case PREVIEW -> PAYMENT;
            case PAYMENT -> null;
        };
    }

    public BookingSessionStep previous() {
        return switch (this) {
            case SEARCH -> null;
            case SELECT_FLIGHT -> SEARCH;
            case INPUT_PASSENGERS -> SELECT_FLIGHT;
            case SELECT_SERVICE -> INPUT_PASSENGERS;
            case PREVIEW -> SELECT_SERVICE;
            case PAYMENT -> PREVIEW;
        };
    }
}