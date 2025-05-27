package com.vietjoke.vn.controller.location;

import com.vietjoke.vn.entity.fleet.RouteEntity;
import com.vietjoke.vn.repository.fleet.RouteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/location")
public class RouteController {

    private final RouteRepository routeRepository;

    @Operation(
            summary = "Get all routes",
            description = "Retrieve a list of all available routes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of routes retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RouteEntity.class))
            )
    })
    @GetMapping("/routes")
    public ResponseEntity<?> getAllAirports(){
        List<RouteEntity> responseDTO = routeRepository.findAll();
        return ResponseEntity.ok(responseDTO);
    }

}
