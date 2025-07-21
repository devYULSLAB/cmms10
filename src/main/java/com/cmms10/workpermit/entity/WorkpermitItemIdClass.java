package com.cmms10.workpermit.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 패키지명: com.cmms10.workPermit.entity
 * 파일명: WorkPermitItemIdClass.java
 * 프로그램명: 작업허가 항목 PK 클래스
 * 주요기능: workPermitItem 테이블의 복합키 매핑
 * 생성자: cmms10
 * 생성일: 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkpermitItemIdClass implements Serializable {
    private String companyId;
    private String permitId;
    private String itemId;
}
