package com.vietjoke.vn.controller.location;

import com.vietjoke.vn.entity.fleet.RouteEntity;
import com.vietjoke.vn.repository.fleet.RouteRepository;
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

    @GetMapping("/routes")
    public ResponseEntity<?> getAllAirports(){
        List<RouteEntity> responseDTO = routeRepository.findAll();
        return ResponseEntity.ok(responseDTO);
    }

}
