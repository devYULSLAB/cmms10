package com.cmms10.inventoryMaster.repository;

import com.cmms10.inventoryMaster.entity.InventoryStock;
import com.cmms10.inventoryMaster.entity.InventoryStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

/**
 * cmms10 - InventoryStockRepository
 * 재고(InventoryStock) JPA 레포지토리 및 Pessimistic Lock 메서드
 *
 * @author cmms10
 * @since 2024-07-13
 */
public interface InventoryStockRepository extends JpaRepository<InventoryStock, InventoryStockId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from InventoryStock s where s.companyId = :companyId and s.siteId = :siteId and s.locId = :locId and s.inventoryId = :inventoryId")
    Optional<InventoryStock> findWithLock(@Param("companyId") String companyId,
            @Param("siteId") String siteId,
            @Param("locId") String locId,
            @Param("inventoryId") String inventoryId);
}
