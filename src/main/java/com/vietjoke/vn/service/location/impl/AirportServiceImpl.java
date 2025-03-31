package com.vietjoke.vn.service.location.impl;

import com.vietjoke.vn.converter.AirportConverter;
import com.vietjoke.vn.dto.location.AirportDTO;
import com.vietjoke.vn.dto.response.ResponseDTO;
import com.vietjoke.vn.exception.data.DataNotFoundException;
import com.vietjoke.vn.repository.location.AirportRepository;
import com.vietjoke.vn.service.location.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportConverter airportConverter;

    private final AirportRepository airportRepository;

    @Override
    public ResponseDTO<List<AirportDTO>> getAllAirports() {
        try{
            List<AirportDTO> airportDTOS = airportRepository.findAll().stream()
                    .map(airportConverter::toAirportDTO)
                    .toList();

            if(airportDTOS.isEmpty()){
                throw new DataNotFoundException("No airports found");
            }
            return ResponseDTO.success(airportDTOS);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
