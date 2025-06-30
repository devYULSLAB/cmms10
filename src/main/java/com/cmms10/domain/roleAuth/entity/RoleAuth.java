package com.cmms10.domain.roleAuth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Objects;

/**
 * cmms10 - RoleAuth
 * 역할 권한 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Entity
@Table(name = "roleAuth")
@IdClass(RoleAuthIdClass.class)
@Getter
@Setter
@NoArgsConstructor
public class RoleAuth {

    @Id
    @Column(name = "roleId", length = 5, nullable = false)
    private String roleId;

    @Column(name = "roleName", length = 50)
    private String roleName;

    @Id
    @Column(name = "authGranted", length = 50, nullable = false)
    private String authGranted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleAuth that = (RoleAuth) o;
        return Objects.equals(roleId, that.roleId) &&
               Objects.equals(authGranted, that.authGranted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, authGranted);
    }
}
