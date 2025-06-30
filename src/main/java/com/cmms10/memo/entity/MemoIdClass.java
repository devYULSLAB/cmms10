package com.cmms10.memo.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * cmms10 - MemoIdClass
 * 메모 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
public class MemoIdClass implements Serializable {
    private String companyId;
    private String memoId;

    // Constructors
    public MemoIdClass() {
    }

    public MemoIdClass(String companyId, String memoId) {
        this.companyId = companyId;
        this.memoId = memoId;
    }

    // Getters and Setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMemoId() {
        return memoId;
    }

    public void setMemoId(String memoId) {
        this.memoId = memoId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoIdClass that = (MemoIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(memoId, that.memoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, memoId);
    }
}
