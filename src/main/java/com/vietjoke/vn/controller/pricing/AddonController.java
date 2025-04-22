package com.vietjoke.vn.controller.pricing;

import com.vietjoke.vn.service.pricing.AddonService;
import com.vietjoke.vn.util.enums.pricing.AddonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AddonController {

    private final AddonService addonService;

    @GetMapping("/addons")
    public ResponseEntity<?> getAddons(@RequestParam String addonCode,
                                       @RequestParam(defaultValue = "name") String sortBy,
                                       @RequestParam(defaultValue = "asc") String sortOrder,
                                       @RequestParam(defaultValue = "1") int pageNumber,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(defaultValue = "ACTIVE") AddonStatus status) {
        return ResponseEntity.ok(addonService.getAddonsByType(addonCode, sortBy, sortOrder, pageNumber, pageSize, status));
    }

}
