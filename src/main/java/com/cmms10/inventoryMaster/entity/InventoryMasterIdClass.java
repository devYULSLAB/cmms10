package com.cmms10.inventoryMaster.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMasterIdClass implements Serializable {
    private String companyId;
    private String inventoryId;
}
