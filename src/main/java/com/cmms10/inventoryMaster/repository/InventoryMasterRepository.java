package com.cmms10.inventoryMaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryMasterIdClass;

import java.util.Optional;
import jakarta.persistence.LockModeType;

@Repository
public interface InventoryMasterRepository extends JpaRepository<InventoryMaster, InventoryMasterIdClass> {

    /**
     * 회사 ID별 최대 inventory ID를 조회합니다.
     *
     * @param companyId 회사 ID (재고는 회사단위로 관리하며 사이트 별로 관리하지 않음)
     * @return Optional<String> 최대 inventory ID
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(p.inventoryId) FROM InventoryMaster p WHERE p.companyId = :companyId")
    String findMaxInventoryIdByCompanyId(@Param("companyId") String companyId);

    /**
     * 회사 ID와 재고 ID를 기준으로 재고 마스터를 조회합니다.
     *
     * @param companyId   회사 ID
     * @param inventoryId 재고 ID
     * @return Optional<InventoryMaster> 조회된 재고 마스터, 없으면 Optional.empty()
     */
    Optional<InventoryMaster> findByCompanyIdAndInventoryIdAndDeleteMarkIsNull(String companyId, String inventoryId);

    /**
     * 회사 ID를 기준으로 재고 마스터 목록을 조회합니다.
     *
     * @param companyId 회사 ID
     * @param pageable  페이징 정보
     * @return Page<InventoryMaster> 조회된 재고 마스터 목록
     */
    Page<InventoryMaster> findByCompanyIdAndDeleteMarkIsNull(String companyId, Pageable pageable);

    /**
     * 회사 ID와 담당 부서를 기준으로 재고 마스터 목록을 조회합니다.
     *
     * @param companyId 회사 ID
     * @param respDept  담당 부서
     * @param pageable  페이징 정보
     * @return Page<InventoryMaster> 조회된 재고 마스터 목록
     */
    Page<InventoryMaster> findByCompanyIdAndRespDeptAndDeleteMarkIsNull(String companyId, String respDept,
            Pageable pageable);

    /**
     * 회사 ID와 재고명을 기준으로 재고 마스터 목록을 조회합니다.
     *
     * @param companyId     회사 ID
     * @param inventoryName 재고명 (부분 일치)
     * @param pageable      페이징 정보
     * @return Page<InventoryMaster> 조회된 재고 마스터 목록
     */
    Page<InventoryMaster> findByCompanyIdAndInventoryNameContainingAndDeleteMarkIsNull(String companyId,
            String inventoryName, Pageable pageable);

}
