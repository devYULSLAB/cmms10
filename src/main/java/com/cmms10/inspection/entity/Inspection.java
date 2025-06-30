package com.cmms10.inspection.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * cmms10 - Inspection
 * 점검 계획 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Entity
@Table(name = "inspection")
@IdClass(InspectionIdClass.class)
@Getter
@Setter
@NoArgsConstructor
public class Inspection {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "inspectionId", length = 10, nullable = false)
    private String inspectionId;

    @Column(name = "inspectionName", length = 100)
    private String inspectionName;

    @Column(name = "plantId", length = 10)
    private String plantId;

    @Column(name = "jobType", length = 5)
    private String jobType;

    @Column(name = "dept", length = 5)
    private String dept;

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

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InspectionItem> items = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inspection that = (Inspection) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(inspectionId, that.inspectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, inspectionId);
    }

}
