package com.cmms10.inventoryMaster.dto;

import java.util.ArrayList;
import java.util.List;

import com.cmms10.inventoryMaster.entity.InventoryHistory;

/**
 * 재고 거래 공통 커맨드(래퍼) 객체
 * - HTML의 name="inventoryHistoryList[0].inventoryId" 등과 1:1 매칭
 * - 리스트를 미리 new ArrayList<>()로 초기화하여 바인딩 실패 시 NPE 방지
 */
public class InventoryTransactionForm {

    /** 화면 hidden field 로 전달되는 siteId */
    private String siteId;

    /** 거래 행들(입고/출고/이동/조정 공통) */
    private List<InventoryHistory> inventoryHistoryList = new ArrayList<>();

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public List<InventoryHistory> getInventoryHistoryList() {
        return inventoryHistoryList;
    }

    public void setInventoryHistoryList(List<InventoryHistory> inventoryHistoryList) {
        this.inventoryHistoryList = (inventoryHistoryList == null) ? new ArrayList<>() : inventoryHistoryList;
    }
}
