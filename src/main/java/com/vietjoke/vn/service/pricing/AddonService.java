package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.dto.pricing.AddonDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.util.enums.pricing.AddonStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AddonService {
    ResponseDTO<Page<AddonDTO>> getAddonsByType(
            String addonCode,
            String sortBy,
            String sortOrder,
            int pageNumber,
            int pageSize,
            AddonStatus addonStatus);
}
