package com.cmms10.domain.dept.entity;

import java.io.Serializable;
import java.util.Objects;

public class DeptIdClass implements Serializable {
    private String companyId;
    private String deptId;

    // Default constructor
    public DeptIdClass() {
    }

    // Constructor with fields
    public DeptIdClass(String companyId, String deptId) {
        this.companyId = companyId;
        this.deptId = deptId;
    }

    // Getters and setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    // hashCode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptIdClass that = (DeptIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(deptId, that.deptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, deptId);
    }
}
