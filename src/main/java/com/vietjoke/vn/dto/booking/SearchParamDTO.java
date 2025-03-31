package com.vietjoke.vn.dto.booking;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SearchParamDTO {

    @NotNull(message = "Trip type is required")
    private String tripType;

    @NotNull(message = "Departure location is required")
    private String tripFrom;

    @NotNull(message = "Destination location is required")
    private String tripTo;

    @NotNull(message = "Start date is required")
    private LocalDateTime tripStartDate;

    private LocalDateTime tripReturnDate;

    @Min(value = 1, message = "At least 1 passenger is required")
    private Integer tripPassengers = 1;

    @Min(value = 1, message = "At least 1 adult passenger is required")
    private Integer tripPassengersAdult = 1;

    @Min(value = 0, message = "Number of child passengers cannot be negative")
    private Integer tripPassengersChild = 0;

    @Min(value = 0, message = "Number of infant passengers cannot be negative")
    private Integer tripPassengersInfant = 0;
    private String coupon;
    private boolean is_find_cheapest = false;

    @AssertTrue(message = "Start date must be today or in the future")
    public boolean isStartDateValid() {
        if (tripStartDate == null) {
            return true;
        }
        try{
            LocalDate startDate = tripStartDate.toLocalDate();
            return !startDate.isBefore(LocalDate.now());
        }
        catch (Exception e) {
            return false;
        }
    }
}