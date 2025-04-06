package com.vietjoke.vn.exception.booking;

public class InsufficientSeatAvailabilityException extends RuntimeException {
  public InsufficientSeatAvailabilityException(String message) {
    super(message);
  }
}
