package com.cmms10.domain.roleAuth.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAuthIdClass implements Serializable {
    private String roleId;
    private String authGranted;
}
