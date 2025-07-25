package com.cmms10.workpermit.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 패키지명: com.cmms10.workPermit.entity
 * 파일명: WorkPermit.java
 * 프로그램명: 작업허가 관리
 * 주요기능: 작업허가서(workPermit) 테이블 매핑 엔티티
 * 생성자: cmms10
 * 생성일: 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workPermit")
@IdClass(WorkpermitIdClass.class)
public class Workpermit {
    /** 회사 ID (PK) */
    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    /** 사이트 ID (PK) */
    @Id
    @Column(name = "siteId", length = 5, nullable = false)
    private String siteId;

    /** 작업허가서 ID (PK) */
    @Id
    @Column(name = "permitId", length = 10, nullable = false)
    private String permitId;

    /** 작업허가서 이름 */
    @Column(name = "permitName", length = 100)
    private String permitName;

    /** 설비 ID */
    @Column(name = "plantId", length = 10, nullable = false)
    private String plantId;

    /** 작업 유형 코드 */
    @Column(name = "permitType", length = 5)
    private String permitType;

    /** 부서 ID */
    @Column(name = "deptId", length = 5)
    private String deptId;

    /** 상태: D(초안), P(진행중), A(승인), C(종료), R(반려) */
    @Column(name = "status", length = 1)
    private String status;

    /** 작업 시작 일시 */
    @Column(name = "startDate")
    private LocalDate startDate;

    /** 작업 종료 일시 */
    @Column(name = "endDate")
    private LocalDate endDate;

    /** 작성자 (username) */
    @Column(name = "requestBy", length = 5)
    private String requestBy;

    /** 승인자 (username) */
    @Column(name = "approveBy", length = 5)
    private String approveBy;

    /** 실제 작업자 (username) */
    @Column(name = "performBy", length = 5)
    private String performBy;

    /** 위험 요소 */
    @Column(name = "hazardNote", columnDefinition = "TEXT")
    private String hazardNote;

    /** 안전조치 */
    @Column(name = "safetyMeasure", columnDefinition = "TEXT")
    private String safetyMeasure;

    /** 작업 요약 */
    @Column(name = "workDesc", columnDefinition = "TEXT")
    private String workDesc;

    /** 첨부파일 그룹 */
    @Column(name = "fileGroupId", length = 10)
    private String fileGroupId;

    /** 생성자 */
    @Column(name = "createBy", length = 5)
    private String createBy;

    /** 생성일 */
    @Column(name = "createDate")
    private LocalDateTime createDate;

    /** 수정자 */
    @Column(name = "updateBy", length = 5)
    private String updateBy;

    /** 수정일 */
    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "workpermit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkpermitItem> items = new ArrayList<>();

}