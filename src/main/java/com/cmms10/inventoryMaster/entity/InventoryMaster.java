package com.cmms10.inventoryMaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * cmms10 - InventoryMaster
 * 재고 마스터 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventoryMaster")
@IdClass(InventoryMasterIdClass.class)
public class InventoryMaster {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "inventoryId", length = 10, nullable = false)
    private String inventoryId;

    @Column(name = "inventoryName", length = 100)
    private String inventoryName;

    @Column(name = "respDept", length = 5)
    private String respDept;

    @Column(name = "assetType", length = 5)
    private String assetType;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "manufacturerModel", length = 100)
    private String manufacturerModel;

    @Column(name = "manufacturerSN", length = 100)
    private String manufacturerSN;

    @Column(name = "manufacturerSpec", length = 100)
    private String manufacturerSpec;

    @Lob
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

    @Column(name = "deleteMark", length = 1)
    private String deleteMark;

}
