package com.vietjoke.vn.config.seeding.jsonObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FareClass {
    private String code;
    private String name;
    private String description;

    @JsonProperty("isActive")
    private boolean isActive;
    private int baggageAllowance;
    private boolean seatSelection;
    private boolean mealIncluded;
    private boolean changeAllowed;
    private boolean refundAllowed;
    private Float changeFee;
    private Float refundFee;
    private String airlineCode;
}