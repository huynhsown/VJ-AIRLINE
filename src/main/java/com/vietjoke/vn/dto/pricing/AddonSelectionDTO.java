package com.vietjoke.vn.dto.pricing;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddonSelectionDTO {
    @NotNull(message = "Addon ID is required")
    private Long addonId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}
