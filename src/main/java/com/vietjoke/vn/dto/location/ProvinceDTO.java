package com.vietjoke.vn.dto.location;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProvinceDTO extends BaseDTO {
    private String provinceCode;
    private String provinceName;
    private String provinceEngName;
    private CountryDTO country;
}