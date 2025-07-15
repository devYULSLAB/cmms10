package com.cmms10.inventoryMaster.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHistoryId implements Serializable {
    private String companyId;
    private String historyId;
}
