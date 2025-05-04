package com.vietjoke.vn.util.enums.booking;

public enum BookingStatus {

    /**
     * The booking has been created but not yet paid.
     * Waiting for payment or further confirmation from the user.
     */
    PENDING,

    /**
     * The booking has been successfully paid and confirmed.
     * Seats and passenger information are secured.
     */
    CONFIRMED,

    /**
     * The booking was cancelled either by the user or the system.
     * No longer valid.
     */
    CANCELLED,

    /**
     * The flight associated with the booking has been completed.
     * Booking remains in the system for record-keeping.
     */
    COMPLETED,

    /**
     * The booking expired due to no action taken within the allowed time.
     * Typically happens when payment was not made in time.
     */
    EXPIRED
}
