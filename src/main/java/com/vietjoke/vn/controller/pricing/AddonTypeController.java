package com.vietjoke.vn.controller.pricing;

import com.vietjoke.vn.service.pricing.AddonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AddonTypeController {

    private final AddonTypeService addonTypeService;

    @GetMapping("/addon/types")
    public ResponseEntity<?> getAddonTypes(@RequestParam(required = false) String sessionToken) {
        return ResponseEntity.ok(addonTypeService.getAllAddonTypes(sessionToken));
    }

}
