package com.cmms10.inspection.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * cmms10 - InspectionItem
 * 점검 항목 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inspectionItem")
@IdClass(InspectionItemIdClass.class) // This will need InspectionItemIdClass in the same package or imported
public class InspectionItem {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "inspectionId", length = 10, nullable = false)
    private String inspectionId;

    @Id
    @Column(name = "itemId", length = 2, nullable = false)
    private String itemId;

    @Column(name = "itemName", length = 100)
    private String itemName;

    @Column(name = "itemMethod", length = 100)
    private String itemMethod;

    @Column(name = "itemUnit", length = 10)
    private String itemUnit;

    @Column(name = "itemLower", precision = 15, scale = 2) // Assuming precision and scale
    private BigDecimal itemLower;

    @Column(name = "itemUpper", precision = 15, scale = 2) // Assuming precision and scale
    private BigDecimal itemUpper;

    @Column(name = "itemStandard", precision = 15, scale = 2) // Assuming precision and scale
    private BigDecimal itemStandard;

    @Column(name = "itemResult", precision = 15, scale = 2) // Assuming precision and scale
    private BigDecimal itemResult;

    @Lob // For TEXT type
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "companyId", referencedColumnName = "companyId", insertable = false, updatable = false),
            @JoinColumn(name = "inspectionId", referencedColumnName = "inspectionId", insertable = false, updatable = false)
    })
    private Inspection inspection; // This will need Inspection in the same package or imported
}
