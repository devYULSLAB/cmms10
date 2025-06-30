package com.cmms10.inventoryMaster.entity;

import java.io.Serializable;
import java.util.Objects;

public class InventoryMasterIdClass implements Serializable {
    private String companyId;
    private String inventoryId;

    // Constructors
    public InventoryMasterIdClass() {
    }

    public InventoryMasterIdClass(String companyId, String inventoryId) {
        this.companyId = companyId;
        this.inventoryId = inventoryId;
    }

    // Getters and Setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryMasterIdClass that = (InventoryMasterIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(inventoryId, that.inventoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, inventoryId);
    }
}
