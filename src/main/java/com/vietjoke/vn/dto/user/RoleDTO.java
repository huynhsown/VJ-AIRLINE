package com.vietjoke.vn.dto.user;

import com.vietjoke.vn.dto.core.BaseDTO;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO extends BaseDTO {
    private String roleCode;
    private String roleName;
    private String description;
    private List<UserDTO> users;
}