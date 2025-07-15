package com.cmms10.inventoryMaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryMasterIdClass;

import java.util.Optional;

@Repository
public interface InventoryMasterRepository extends JpaRepository<InventoryMaster, InventoryMasterIdClass> {

    /**
     * 회사 ID별 최대 inventory ID를 조회합니다.
     *
     * @param companyId 회사 ID (재고는 회사단위로 관리하며 사이트 별로 관리하지 않음)
     * @return Optional<String> 최대 inventory ID
     */
    @Query("SELECT MAX(p.inventoryId) FROM InventoryMaster p WHERE p.companyId = :companyId")
    String findMaxInventoryIdByCompanyId(@Param("companyId") String companyId);

    /**
     * Finds a specific non-deleted InventoryMaster by its companyId and inventoryId.
     *
     * @param companyId The ID of the company.
     * @param inventoryId The ID of the inventory.
     * @return An Optional containing the non-deleted InventoryMaster if found, or empty otherwise.
     */

     Optional<InventoryMaster> findByCompanyIdAndInventoryIdAndDeleteMarkIsNull(String companyId, String inventoryId);

    /**
     * Finds a page of non-deleted InventoryMaster entries for a given companyId and siteId.
     *
     * @param companyId The ID of the company.
     * @param pageable Pagination information.
     * @return A page of non-deleted InventoryMaster entities.
     */
    Page<InventoryMaster> findByCompanyIdAndDeleteMarkIsNull(String companyId, Pageable pageable);

    /**
     * Finds all non-deleted InventoryMaster entries for a given companyId.
     *
     * @param companyId The ID of the company.
     * @param respDept The ID of the responsible department.
     * @param pageable Pagination information.
     * @return A list of non-deleted InventoryMaster entities.
     */
    Page<InventoryMaster> findByCompanyIdAndRespDeptAndDeleteMarkIsNull(String companyId, String respDept, Pageable pageable);
    
    /**
     * Finds all non-deleted InventoryMaster entries for a given companyId.
     *
     * @param companyId The ID of the company.
     * @param inventoryName The name of the inventory (partial match).
     * @param pageable Pagination information.
     * @return A list of non-deleted InventoryMaster entities.
     */
    Page<InventoryMaster> findByCompanyIdAndInventoryNameContainingAndDeleteMarkIsNull(String companyId, String inventoryName, Pageable pageable);
    
}
