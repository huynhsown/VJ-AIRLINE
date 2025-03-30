package com.vietjoke.vn.controller.booking;

import com.vietjoke.vn.dto.booking.SearchParamDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SearchController {
    @GetMapping("/search")
    public ResponseEntity<?> searchFlight(@Valid @RequestBody SearchParamDTO param){
        return null;
    }
}
