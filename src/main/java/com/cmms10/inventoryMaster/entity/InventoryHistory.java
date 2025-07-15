package com.cmms10.inventoryMaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @Column(name = "historyId", length = 10, nullable = false)
    private String historyId;

    @Column(name = "siteId", length = 5)
    private String siteId;

    @Column(name = "locId", length = 5)
    private String locId;

    @Column(name = "inventoryId", length = 10)
    private String inventoryId;

    @Column(name = "ioType", length = 1)
    private String ioType;

    @Column(name = "ioDate")
    private LocalDateTime ioDate;

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
