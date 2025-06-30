package com.cmms10.workorder.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * cmms10 - workorderIdClass
 * 작업 오더 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
public class WorkorderIdClass implements Serializable {
    private String companyId;
    private String orderId;

    // Constructors
    public WorkorderIdClass() {
    }

    public WorkorderIdClass(String companyId, String orderId) {
        this.companyId = companyId;
        this.orderId = orderId;
    }

    // Getters and Setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkorderIdClass that = (WorkorderIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, orderId);
    }
}
