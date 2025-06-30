package com.cmms10.commonCode.entity;

import java.io.Serializable;
import java.util.Objects;

public class CommonCodeIdClass implements Serializable {
    private String companyId;
    private String codeId;

    // Constructors
    public CommonCodeIdClass() {
    }

    public CommonCodeIdClass(String companyId, String codeId) {
        this.companyId = companyId;
        this.codeId = codeId;
    }

    // Getters and Setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonCodeIdClass that = (CommonCodeIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(codeId, that.codeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, codeId);
    }
}
