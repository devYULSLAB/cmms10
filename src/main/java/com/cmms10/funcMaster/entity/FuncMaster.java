package com.cmms10.funcMaster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 프로그램명: CMMS10
 * 패키지명: com.cmms10.funcMaster.entity
 * 클래스명: FuncMaster
 * 주요기능: 기능 마스터 엔티티 (PlantMaster의 funcId 관리)
 * 생성자: AI Assistant
 * 생성일: 2024-12-19
 */
@Entity
@Table(name = "funcMaster")
@IdClass(FuncMasterIdClass.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncMaster {

    @Id
    @Column(name = "companyId", length = 5)
    private String companyId;

    @Id
    @Column(name = "siteId", length = 5)
    private String siteId;

    @Id
    @Column(name = "funcId", length = 5)
    private String funcId;

    @Column(name = "funcType", length = 5)
    private String funcType;

    @Column(name = "funcName", length = 100)
    private String funcName;
}