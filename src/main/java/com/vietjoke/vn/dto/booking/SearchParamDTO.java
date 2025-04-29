package com.vietjoke.vn.dto.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vietjoke.vn.constant.PassengerConstants;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchParamDTO {

    @NotNull(message = "Trip type is required")
    @Pattern(regexp = "oneway|round_trip", message = "Trip type must be either 'oneway' or 'round_trip'")
    private String tripType;

    @NotNull(message = "Departure location is required")
    private String tripFrom;

    @NotNull(message = "Destination location is required")
    private String tripTo;

    @NotNull(message = "Start date is required")
    private LocalDate tripStartDate;

    private LocalDate tripReturnDate;

    @Min(value = PassengerConstants.MIN_PASSENGERS, message = "At least 1 passenger is required")
    @Max(value = PassengerConstants.MAX_PASSENGERS, message = "Total passengers cannot exceed " + PassengerConstants.MAX_PASSENGERS)
    private Integer tripPassengers = PassengerConstants.MIN_PASSENGERS;

    @Min(value = PassengerConstants.MIN_ADULT_PASSENGERS, message = "At least 1 adult passenger is required")
    @Max(value = PassengerConstants.MAX_ADULT_PASSENGERS, message = "Adult passengers cannot exceed " + PassengerConstants.MAX_ADULT_PASSENGERS)
    private Integer tripPassengersAdult = PassengerConstants.MIN_ADULT_PASSENGERS;

    @Min(value = PassengerConstants.MIN_CHILD_PASSENGERS, message = "Number of child passengers cannot be negative")
    @Max(value = PassengerConstants.MAX_CHILD_PASSENGERS, message = "Child passengers cannot exceed " + PassengerConstants.MAX_CHILD_PASSENGERS)
    private Integer tripPassengersChild = PassengerConstants.MIN_CHILD_PASSENGERS;

    @Min(value = PassengerConstants.MIN_INFANT_PASSENGERS, message = "Number of infant passengers cannot be negative")
    private Integer tripPassengersInfant = PassengerConstants.MIN_INFANT_PASSENGERS;

    @JsonIgnore
    private String coupon;

    @JsonIgnore
    private boolean is_find_cheapest = false;

    @JsonIgnore
    @AssertTrue(message = "Start date must be today or in the future")
    public boolean isStartDateValid() {
        return !tripStartDate.isBefore(LocalDate.now());
    }

    @JsonIgnore
    @AssertTrue(message = "Return date must be equal to or after start date")
    public boolean isReturnDateValid() {
        return tripReturnDate == null || !tripReturnDate.isBefore(tripStartDate);
    }

    @JsonIgnore
    @AssertTrue(message = "Departure and destination locations must be different")
    public boolean isTripLocationsValid() {
        return !tripFrom.equalsIgnoreCase(tripTo);
    }

    @JsonIgnore
    @AssertTrue(message = "Total passengers must match the sum of adults, children, and infants")
    public boolean isTotalPassengersValid() {
        int adults = tripPassengersAdult != null ? tripPassengersAdult : 0;
        int children = tripPassengersChild != null ? tripPassengersChild : 0;
        int infants = tripPassengersInfant != null ? tripPassengersInfant : 0;
        int total = tripPassengers != null ? tripPassengers : 0;
        return adults + children + infants == total;
    }

    @JsonIgnore
    @AssertTrue(message = "Number of infants must not exceed number of adults")
    public boolean isChildrenVsAdultsValid() {
        int adults = tripPassengersAdult != null ? tripPassengersAdult : 0;
        int children = tripPassengersChild != null ? tripPassengersChild : 0;
        return adults + children <= PassengerConstants.MAX_PASSENGERS;
    }

    @JsonIgnore
    @AssertTrue(message = "Number of infants must not exceed the limit per adult")
    public boolean isInfantsPerAdultValid() {
        int adults = tripPassengersAdult != null ? tripPassengersAdult : 0;
        int infants = tripPassengersInfant != null ? tripPassengersInfant : 0;
        return infants <= adults * PassengerConstants.MAX_INFANT_PER_ADULT;
    }
}