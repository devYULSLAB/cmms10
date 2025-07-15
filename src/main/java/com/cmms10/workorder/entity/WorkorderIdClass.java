package com.cmms10.workorder.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * cmms10 - workorderIdClass
 * 작업 오더 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkorderIdClass implements Serializable {
    private String companyId;
    private String orderId;
}
