package com.cmms10.plantMaster.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Objects;

/**
 * cmms10 - PlantMaster
 * 설비 마스터 정보 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Getter
@Setter
@Entity
@Table(name = "plantMaster")
@IdClass(PlantMasterIdClass.class)
public class PlantMaster {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "plantId", length = 10, nullable = false)
    private String plantId;

    @Column(name = "plantName", length = 100)
    private String plantName;

    @Column(name = "plantLoc", length = 100)
    private String plantLoc;

    @Column(name = "funcId", length = 4)
    private String funcId;

    @Column(name = "respDept", length = 5)
    private String respDept;

    @Column(name = "installDate")
    private LocalDate installDate;

    @Column(name = "assetType", length = 5)
    private String assetType;

    @Column(name = "depreMethod", length = 5)
    private String depreMethod;

    @Column(name = "acquitionValue", precision = 15, scale = 2)
    private BigDecimal acquitionValue;

    @Column(name = "residualValue", precision = 15, scale = 2)
    private BigDecimal residualValue;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "manufacturerModel", length = 100)
    private String manufacturerModel;

    @Column(name = "manufacturerSN", length = 100)
    private String manufacturerSN;

    @Column(name = "manufacturerSpec", length = 100)
    private String manufacturerSpec;

    @Column(name = "inspectionYN", length = 1)
    private String inspectionYN;

    @Column(name = "plannedMaintenanceYN", length = 1)
    private String plannedMaintenanceYN;

    @Column(name = "psmYN", length = 1)
    private String psmYN;

    @Column(name = "tagYN", length = 1)
    private String tagYN;

    @Lob // For TEXT type
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "siteId", length = 5)
    private String siteId;

    @Column(name = "fileGroupId", length = 10)
    private String fileGroupId;

    @Column(name = "createBy", length = 5)
    private String createBy;

    @Column(name = "createDate")
    private LocalDateTime createDate;

    @Column(name = "updateBy", length = 5)
    private String updateBy;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Column(name = "deleteMark")
    private String deleteMark;

    // Constructors
    public PlantMaster() {
    }

    public PlantMaster(String companyId, String plantId) {
        this.companyId = companyId;
        this.plantId = plantId;
    }

    public PlantMaster(String companyId, String plantId, String plantName) {
        this.companyId = companyId;
        this.plantId = plantId;
        this.plantName = plantName;
    }

    // equals and hashCode (only for PK fields)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantMaster that = (PlantMaster) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(plantId, that.plantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, plantId);
    }
}
