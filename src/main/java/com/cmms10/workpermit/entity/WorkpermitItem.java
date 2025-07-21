package com.cmms10.workpermit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 패키지명: com.cmms10.workPermit.entity
 * 파일명: WorkPermitItem.java
 * 프로그램명: 작업허가 항목 관리
 * 주요기능: 작업허가서 항목(workPermitItem) 테이블 매핑 엔티티
 * 생성자: cmms10
 * 생성일: 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workPermitItem")
@IdClass(WorkpermitItemIdClass.class)
public class WorkpermitItem implements Serializable {
    /** 회사 ID (PK) */
    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    /** 작업허가서 ID (PK) */
    @Id
    @Column(name = "permitId", length = 10, nullable = false)
    private String permitId;

    /** 항목 번호 (PK, 자동 증가) */
    @Id
    @Column(name = "itemId", length = 2, nullable = false)
    private String itemId;

    /** 서명자 이름 */
    @Column(name = "signerName", length = 100)
    private String signerName;

    /** 서명 데이터 (이미지 또는 base64) */
    @Lob
    @Column(name = "signature")
    private String signature;

    /** 서명 시간 */
    @Column(name = "signedAt")
    private LocalDateTime signedAt;

    /** 역할 (WRITER, APPROVER, WORKER 등) */
    @Column(name = "role", length = 5)
    private String role;

    /** 비고 */
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "companyId", referencedColumnName = "companyId", insertable = false, updatable = false),
        @JoinColumn(name = "permitId", referencedColumnName = "permitId", insertable = false, updatable = false)
    })
    private Workpermit workpermit;
}
