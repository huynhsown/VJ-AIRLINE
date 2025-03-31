package com.vietjoke.vn.service.fleet.impl;

import com.vietjoke.vn.entity.fleet.RouteEntity;
import com.vietjoke.vn.repository.fleet.RouteRepository;
import com.vietjoke.vn.service.fleet.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;

    public RouteEntity getRouteEntity(String routeCode) {
        return routeRepository.findByRouteCode(routeCode)
                .orElseThrow(() -> new RuntimeException("Route not found: " + routeCode));
    }
}
