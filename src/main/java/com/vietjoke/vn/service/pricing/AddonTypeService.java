package com.vietjoke.vn.service.pricing;

import com.vietjoke.vn.dto.booking.SessionTokenRequestDTO;
import com.vietjoke.vn.dto.pricing.AddonTypeDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;

import java.util.List;

public interface AddonTypeService {
    ResponseDTO<List<AddonTypeDTO>> getAllAddonTypes(String sessionToken);
}
