package com.cmms10.inspection.entity; // << UPDATED PACKAGE

import java.io.Serializable;
import java.util.Objects;

public class InspectionIdClass implements Serializable {
    private String companyId;
    private String inspectionId;

    // Constructors
    public InspectionIdClass() {
    }

    public InspectionIdClass(String companyId, String inspectionId) {
        this.companyId = companyId;
        this.inspectionId = inspectionId;
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

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InspectionIdClass that = (InspectionIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(inspectionId, that.inspectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, inspectionId);
    }
}
