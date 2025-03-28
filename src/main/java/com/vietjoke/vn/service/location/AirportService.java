package com.vietjoke.vn.service.location;

import com.vietjoke.vn.dto.location.AirportDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;

import java.util.List;

public interface AirportService {

    ResponseDTO<List<AirportDTO>> getAllAirports();

}
