package com.cmms10.domain.dept.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptIdClass implements Serializable {
    private String companyId;
    private String deptId;

    // hashCode and equals
    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;
    //     DeptIdClass that = (DeptIdClass) o;
    //     return Objects.equals(companyId, that.companyId) &&
    //            Objects.equals(deptId, that.deptId);
    // }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(companyId, deptId);
    // }
}
