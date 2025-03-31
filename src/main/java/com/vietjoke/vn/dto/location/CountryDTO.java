package com.vietjoke.vn.dto.location;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CountryDTO extends BaseDTO {
    private String countryCode;
    private String countryName;
    private String countryEngName;
    private String areaCode;
}