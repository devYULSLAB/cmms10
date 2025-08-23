package com.cmms10.inventoryMaster.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class InventoryStockId implements Serializable {
    private String companyId;
    private String siteId;
    private String locId;
    private String inventoryId;

    @Override
    public String toString() {
        return "InventoryStockId{" +
                "companyId='" + companyId + '\'' +
                ", siteId='" + siteId + '\'' +
                ", locId='" + locId + '\'' +
                ", inventoryId='" + inventoryId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        InventoryStockId that = (InventoryStockId) o;
        return Objects.equals(companyId, that.companyId) &&
                Objects.equals(siteId, that.siteId) &&
                Objects.equals(locId, that.locId) &&
                Objects.equals(inventoryId, that.inventoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, siteId, locId, inventoryId);
    }

    // Getter 메서드들
    public String getCompanyId() {
        return companyId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getLocId() {
        return locId;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    // Setter 메서드들
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }
}