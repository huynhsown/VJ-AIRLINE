package com.vietjoke.vn.controller.pricing;

import com.vietjoke.vn.dto.pricing.AddonTypeDTO;
import com.vietjoke.vn.dto.response.ErrorResponseDTO;
import com.vietjoke.vn.service.pricing.AddonTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AddonTypeController {

    private final AddonTypeService addonTypeService;

    @Operation(
            summary = "Get all addon types",
            description = "Retrieve a list of all addon types. If a session token is provided, validate the booking session and steps."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of addon types retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AddonTypeDTO.class, type = "array"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session expired or not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "412",
                    description = "Missing or invalid booking step",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/addon/types")
    public ResponseEntity<?> getAddonTypes(@RequestParam(required = false) String sessionToken) {
        return ResponseEntity.ok(addonTypeService.getAllAddonTypes(sessionToken));
    }

}
