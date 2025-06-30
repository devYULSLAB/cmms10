package com.cmms10.commonCode.entity;

import java.io.Serializable;
import java.util.Objects;

public class CommonCodeItemIdClass implements Serializable {
    private String companyId;
    private String codeId;
    private String codeItemId;

    // Constructors
    public CommonCodeItemIdClass() {
    }

    public CommonCodeItemIdClass(String companyId, String codeId, String codeItemId) {
        this.companyId = companyId;
        this.codeId = codeId;
        this.codeItemId = codeItemId;
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

    public String getCodeItemId() {
        return codeItemId;
    }

    public void setCodeItemId(String codeItemId) {
        this.codeItemId = codeItemId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonCodeItemIdClass that = (CommonCodeItemIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(codeId, that.codeId) &&
               Objects.equals(codeItemId, that.codeItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, codeId, codeItemId);
    }
}
