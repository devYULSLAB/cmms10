package com.cmms10.plantMaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * cmms10 - PlantMaster
 * 설비 마스터 정보 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plantMaster")
@IdClass(PlantMasterIdClass.class)
public class PlantMaster {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "siteId", length = 5, nullable = false)
    private String siteId;

    @Id
    @Column(name = "plantId", length = 10, nullable = false)
    private String plantId;

    @Column(name = "plantName", length = 100)
    private String plantName;

    @Column(name = "plantLoc", length = 100)
    private String plantLoc;

    @Column(name = "funcId", length = 5)
    private String funcId;

    @Column(name = "respDept", length = 5)
    private String respDept;

    @Column(name = "installDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate installDate;

    @Column(name = "assetType", length = 5)
    private String assetType;

    @Column(name = "depreMethod", length = 5)
    private String depreMethod;

    @Column(name = "acquisitionValue", precision = 15, scale = 2)
    private BigDecimal acquisitionValue;

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

    @Column(name = "workPermitYN", length = 1)
    private String workPermitYN;

    @Column(name = "tagYN", length = 1)
    private String tagYN;

    @Lob // For TEXT type
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

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

}
