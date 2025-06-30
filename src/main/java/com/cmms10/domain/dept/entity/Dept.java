package com.cmms10.domain.dept.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * cmms10 - Dept
 * 부서 정보 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Getter
@Setter
@Entity
@Table(name = "dept")
@IdClass(DeptIdClass.class)
public class Dept {

    @Id
    @Column(name = "companyId", length = 5)
    private String companyId;

    @Id
    @Column(name = "deptId", length = 5)
    private String deptId;

    @Column(name = "deptName", length = 100)
    private String deptName;

    @Column(name = "parentDeptId", length = 5)
    private String parentDeptId;

    @Column(name = "deptLevel")
    private Integer deptLevel;

    @Column(name = "sortOrder")
    private Integer sortOrder;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "siteId", length = 5)
    private String siteId; // As per table structure

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

    // Constructors
    public Dept() {
    }

    public Dept(String companyId, String deptId) {
        this.companyId = companyId;
        this.deptId = deptId;
    }

}
