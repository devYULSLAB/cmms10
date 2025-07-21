package com.cmms10.workorder.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


/**
 * cmms10 - workorder
 * 작업 오더 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workorder")
@IdClass(WorkorderIdClass.class) // This will need WorkorderIdClass in the same package or imported
public class Workorder {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "siteId", length = 5)
    private String siteId;

    @Id
    @Column(name = "orderId", length = 10, nullable = false)
    private String orderId;

    @Column(name = "orderName", length = 100)
    private String orderName;

    @Column(name = "plantId", length = 10)
    private String plantId;

    @Column(name = "memoId", length = 10)
    private String memoId;

    @Column(name = "jobType", length = 5)
    private String jobType;

    @Column(name = "dept", length = 5)
    private String dept;

    @Column(name = "scheduleDate")
    private LocalDate scheduleDate;

    @Column(name = "scheduleMM", precision = 15, scale = 2)
    private BigDecimal scheduleMM;

    @Column(name = "scheduleCost", precision = 15, scale = 2)
    private BigDecimal scheduleCost;

    @Column(name = "scheduleHSE", length = 100)
    private String scheduleHSE;

    @Column(name = "executeDate")
    private LocalDate executeDate;

    @Column(name = "executeMM", precision = 15, scale = 2)
    private BigDecimal executeMM;

    @Column(name = "executeCost", precision = 15, scale = 2)
    private BigDecimal executeCost;

    @Column(name = "executeHSE", length = 100)
    private String executeHSE;

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

    @OneToMany(mappedBy = "workorder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkorderItem> items = new ArrayList<>(); // This will need WorkorderItem in the same package or imported

}
