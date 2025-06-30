package com.cmms10.domain.roleAuth.entity;

import java.io.Serializable;
import java.util.Objects;

public class RoleAuthIdClass implements Serializable {
    private String roleId;
    private String authGranted;

    public RoleAuthIdClass() {}

    public RoleAuthIdClass(String roleId, String authGranted) {
        this.roleId = roleId;
        this.authGranted = authGranted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleAuthIdClass that = (RoleAuthIdClass) o;
        return Objects.equals(roleId, that.roleId) &&
               Objects.equals(authGranted, that.authGranted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, authGranted);
    }
}
