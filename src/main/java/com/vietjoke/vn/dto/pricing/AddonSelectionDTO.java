package com.vietjoke.vn.dto.pricing;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddonSelectionDTO {
    @NotNull(message = "Addon ID is required")
    private Long addonId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must greater than 0")
    private Integer quantity;
}
