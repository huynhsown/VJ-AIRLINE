package com.vietjoke.vn.dto.pricing;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddonTypeDTO extends BaseDTO {
    private String addonCode;
    private String addonName;
}
