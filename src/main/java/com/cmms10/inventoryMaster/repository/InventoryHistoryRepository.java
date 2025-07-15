package com.cmms10.inventoryMaster.repository;

import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.inventoryMaster.entity.InventoryHistoryId;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, InventoryHistoryId> {

    @Query("SELECT MAX(h.historyId) FROM InventoryHistory h WHERE h.companyId = :companyId")
    String findMaxHistoryIdByCompanyId(@Param("companyId") String companyId);

    Page<InventoryHistory> findByCompanyIdAndInventoryIdOrderByIoDateDesc(String companyId, String inventoryId);

}
