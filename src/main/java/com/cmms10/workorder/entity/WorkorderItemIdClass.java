package com.cmms10.workorder.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * cmms10 - workorderItemIdClass
 * 작업 오더 항목 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkorderItemIdClass implements Serializable {
    private String companyId;
    private String orderId;
    private String itemId; // DB is CHAR(2)
}
