package com.cmms10.workpermit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/*
 * 패키지: com.cmms10.workpermit.entity
 * 폴더 구조: src/main/java/com/cmms10/workpermit/entity/
 * 프로그램명: WorkpermitItem.java
 * 주요 기능: 작업허가서 항목 엔티티 (서명자 정보 및 서명 데이터 저장)
 * 생성자: admin
 * 생성일: 2025-01-27
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
    @Column(name = "signature", columnDefinition = "LONGTEXT")
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
