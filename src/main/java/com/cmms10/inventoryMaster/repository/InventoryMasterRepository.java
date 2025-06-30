package com.cmms10.inventoryMaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;

import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryMasterIdClass;

import java.util.Optional;

import jakarta.persistence.LockModeType;

@Repository
public interface InventoryMasterRepository extends JpaRepository<InventoryMaster, InventoryMasterIdClass> {

    /**
     * 회사 ID별 최대 inventory ID를 조회합니다.
     *
     * @param companyId 회사 ID
     * @return Optional<String> 최대 inventory ID
     */
    @Query("SELECT MAX(p.inventoryId) FROM InventoryMaster p WHERE p.companyId = :companyId")
    String findMaxInventoryIdByCompanyId(@Param("companyId") String companyId);

    /**
     * Finds a page of non-deleted InventoryMaster entries for a given companyId and siteId.
     *
     * @param companyId The ID of the company.
     * @param siteId The ID of the site.
     * @param pageable Pagination information.
     * @return A page of non-deleted InventoryMaster entities.
     */
    Page<InventoryMaster> findByCompanyIdAndSiteIdAndDeleteMarkIsNull(String companyId, String siteId, Pageable pageable);

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
     * Finds a specific non-deleted InventoryMaster by its companyId and inventoryId.
     *
     * @param companyId The ID of the company.
     * @param inventoryId The ID of the inventory.
     * @return An Optional containing the non-deleted InventoryMaster if found, or empty otherwise.
     */

    Optional<InventoryMaster> findByCompanyIdAndInventoryIdAndDeleteMarkIsNull(String companyId, String inventoryId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InventoryMaster i WHERE i.companyId = :companyId AND i.inventoryId = :inventoryId")
    Optional<InventoryMaster> findByCompanyIdAndInventoryIdForUpdate(@Param("companyId") String companyId, @Param("inventoryId") String inventoryId);

}
