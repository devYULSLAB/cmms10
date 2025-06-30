package com.cmms10.inventoryMaster.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventoryHistory")
@Getter
@Setter
@IdClass(InventoryHistoryId.class)
public class InventoryHistory {

    @Id
    private String companyId;

    @Id
    private String historyId;

    private String inventoryId;
    private String ioType; // 'I' 또는 'O'
    
    private LocalDateTime ioDate;
    private BigDecimal qty;
    private BigDecimal unitPrice;
    private BigDecimal totalValue;
    private String note;
    private String siteId;
    private String createBy;
    private LocalDateTime createDate;
}
