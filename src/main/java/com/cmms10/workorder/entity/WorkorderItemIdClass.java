package com.cmms10.workorder.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * cmms10 - workorderItemIdClass
 * 작업 오더 항목 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
public class WorkorderItemIdClass implements Serializable {
    private String companyId;
    private String orderId;
    private String itemId; // DB is CHAR(2)

    // Constructors
    public WorkorderItemIdClass() {
    }

    public WorkorderItemIdClass(String companyId, String orderId, String itemId) {
        this.companyId = companyId;
        this.orderId = orderId;
        this.itemId = itemId;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkorderItemIdClass that = (WorkorderItemIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(orderId, that.orderId) &&
               Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, orderId, itemId);
    }
}
