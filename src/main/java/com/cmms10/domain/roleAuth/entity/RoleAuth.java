package com.cmms10.domain.roleAuth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Objects;

/**
 * cmms10 - RoleAuth
 * 역할 권한 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roleAuth")
@IdClass(RoleAuthIdClass.class)
public class RoleAuth {

    @Id
    @Column(name = "roleId", length = 5, nullable = false)
    private String roleId;

    @Column(name = "roleName", length = 50)
    private String roleName;

    @Id
    @Column(name = "authGranted", length = 50, nullable = false)
    private String authGranted;
}
