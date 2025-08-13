/*
 * 패키지: com.cmms10.checksheet.entity
 * 폴더 구조: src/main/java/com/cmms10/checksheet/entity/
 * 프로그램명: ChecksheetResult.java
 * 주요 기능: 체크시트 결과 엔티티 (데이터베이스 테이블 매핑)
 * 생성자: admin
 * 생성일: 2025-01-27
 */

package com.cmms10.checksheet.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@IdClass(ChecksheetResultIdClass.class)
@Table(name = "checksheetresult")
public class ChecksheetResult {

    @Id
    @Column(length = 5)
    private String companyId;

    @Id
    @Column(length = 10)
    private String permitId;

    @Column(length = 10)
    private String templateId;

    @Lob
    private String checkResultJson;

    @Column(length = 5)
    private String createBy;

    private LocalDateTime createDate;
}
