package com.cmms10.inventoryMaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventoryStock")
@IdClass(InventoryStockId.class)
public class InventoryStock {
    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "siteId", length = 5, nullable = false)
    private String siteId;

    @Id
    @Column(name = "locId", length = 5, nullable = false)
    private String locId;

    @Id
    @Column(name = "inventoryId", length = 10, nullable = false)
    private String inventoryId;

    @Column(name = "qty", precision = 15, scale = 2)
    private BigDecimal qty;

    @Column(name = "unitPrice", precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "totalValue", precision = 15, scale = 2)
    private BigDecimal totalValue;
} 