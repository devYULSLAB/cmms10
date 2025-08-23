/*
 * 패키지: com.cmms10.checksheet.entity
 * 폴더 구조: src/main/java/com/cmms10/checksheet/entity/
 * 프로그램명: ChecksheetTemplate.java
 * 주요 기능: 체크시트 템플릿 엔티티 (데이터베이스 테이블 매핑)
 * 생성자: admin
 * 생성일: 2025-01-27
 */

package com.cmms10.checksheet.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "checksheettemplate")
public class ChecksheetTemplate {

    @Id
    @Column(length = 10)
    private String templateId;

    @Column(length = 5, nullable = false)
    private String companyId;

    @Column(length = 100)
    private String templateName;

    @Lob
    private String templateJson; // formBuilder.js 결과(JSON) 전체 저장

    @Column(length = 5)
    private String createBy;

    private LocalDateTime createDate;

    @Column(length = 5)
    private String updateBy;

    private LocalDateTime updateDate;
}
