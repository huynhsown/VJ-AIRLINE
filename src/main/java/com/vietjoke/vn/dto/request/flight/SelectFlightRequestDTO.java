package com.vietjoke.vn.dto.request.flight;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SelectFlightRequestDTO {

    @NotBlank(message = "Session is required")
    private String sessionToken;

    @Valid
    @NotEmpty
    private List<SelectFlightDTO> flights;
}
