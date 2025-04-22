package com.vietjoke.vn.service.pricing.impl;

import com.vietjoke.vn.converter.AddonConverter;
import com.vietjoke.vn.dto.pricing.AddonDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.entity.pricing.AddonEntity;
import com.vietjoke.vn.exception.user.PermissionDenyException;
import com.vietjoke.vn.repository.pricing.AddonRepository;
import com.vietjoke.vn.service.pricing.AddonService;
import com.vietjoke.vn.util.enums.pricing.AddonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddonServiceImpl implements AddonService {

    private final AddonRepository addonRepository;

    private final AddonConverter addonConverter;

    public boolean isUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        // Kiểm tra nếu principal là một instance của UserDetails
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        }

        // Nếu principal không phải UserDetails, xử lý theo nhu cầu (ví dụ: trả false hoặc throw exception)
        return false;
    }

    @Override
    public ResponseDTO<Page<AddonDTO>> getAddonsByType(String addonCode, String sortBy, String sortOrder,
                                                       int pageNumber, int pageSize, AddonStatus addonStatus) {

        boolean isAdmin = isUserAdmin();

        if(addonStatus != AddonStatus.ACTIVE && !isAdmin) {
            throw new PermissionDenyException("FORBIDDEN");
        }

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<AddonEntity> pageAddons = switch (addonStatus) {
            case ACTIVE -> addonRepository.findByAddonTypeEntity_AddonCodeAndIsActive(addonCode, true, pageable);
            case INACTIVE -> addonRepository.findByAddonTypeEntity_AddonCodeAndIsActive(addonCode, false, pageable);
            default -> addonRepository.findByAddonTypeEntity_AddonCode(addonCode, pageable);
        };

        Page<AddonDTO> addonDTOs = pageAddons.map(addonConverter::toAddonDTO);

        return ResponseDTO.success(addonDTOs);
    }
}
