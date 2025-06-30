package com.cmms10.inspection.entity; // << UPDATED PACKAGE

import java.io.Serializable;
import java.util.Objects;

public class InspectionItemIdClass implements Serializable {
    private String companyId;
    private String inspectionId;
    private String itemId; // DB is CHAR(2)

    // Constructors
    public InspectionItemIdClass() {
    }

    public InspectionItemIdClass(String companyId, String inspectionId, String itemId) {
        this.companyId = companyId;
        this.inspectionId = inspectionId;
        this.itemId = itemId;
    }

    // Getters and Setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
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
        InspectionItemIdClass that = (InspectionItemIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(inspectionId, that.inspectionId) &&
               Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, inspectionId, itemId);
    }
}
