package com.cmms10.domain.user.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * cmms10 - UserIdClass
 * 사용자 엔티티의 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
public class UserIdClass implements Serializable {
    private String companyId;
    private String username;

    // Default constructor
    public UserIdClass() {
    }

    // Constructor with fields
    public UserIdClass(String companyId, String username) {
        this.companyId = companyId;
        this.username = username;
    }

    // hashCode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIdClass that = (UserIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, username);
    }
}
