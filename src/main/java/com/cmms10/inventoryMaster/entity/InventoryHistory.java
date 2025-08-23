package com.cmms10.inventoryMaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 재고 이력 엔티티
 * 재고의 입고, 출고, 이동, 조정 이력을 관리
 * 
 * @author cmms10
 * @since 2025-01-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventoryHistory")
@IdClass(InventoryHistoryId.class)
public class InventoryHistory {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "historyId", length = 15, nullable = false)
    private String historyId;

    @Column(name = "siteId", length = 5)
    private String siteId;

    @Column(name = "inventoryId", length = 10)
    private String inventoryId;

    /**
     * 거래 유형
     * IN: 입고, OUT: 출고, MOVE: 이동, ADJUST: 조정
     */
    @Column(name = "transactionType", length = 10, nullable = false)
    private String transactionType;

    /**
     * 출발 위치 (이동/출고 시 사용, 입고/조정 시 NULL)
     */
    @Column(name = "fromLocation", length = 5)
    private String fromLocation;

    /**
     * 도착 위치 (이동/입고 시 사용, 출고/조정 시 NULL)
     */
    @Column(name = "toLocation", length = 5)
    private String toLocation;

    /**
     * 거래일자
     */
    @Column(name = "transactionDate")
    private LocalDateTime transactionDate;

    @Column(name = "qty", precision = 15, scale = 2)
    private BigDecimal qty;

    @Column(name = "unitPrice", precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "totalValue", precision = 15, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "createBy", length = 5)
    private String createBy;

    @Column(name = "createDate")
    private LocalDateTime createDate;
}
