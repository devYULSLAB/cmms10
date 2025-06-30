package com.cmms10.inventoryMaster.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class InventoryHistoryId implements Serializable {
    private String companyId;
    private String historyId;
}
