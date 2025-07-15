package com.cmms10.inventoryMaster.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStockId implements Serializable {
    private String companyId;
    private String siteId;
    private String locId;
    private String inventoryId;
} 