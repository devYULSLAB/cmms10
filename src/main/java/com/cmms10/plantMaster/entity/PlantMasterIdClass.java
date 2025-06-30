package com.cmms10.plantMaster.entity;

import java.io.Serializable;
import java.util.Objects;

public class PlantMasterIdClass implements Serializable {
    private String companyId;
    private String plantId;

    // Constructors
    public PlantMasterIdClass() {
    }

    public PlantMasterIdClass(String companyId, String plantId) {
        this.companyId = companyId;
        this.plantId = plantId;
    }

    // Getters and Setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantMasterIdClass that = (PlantMasterIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(plantId, that.plantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, plantId);
    }
}
