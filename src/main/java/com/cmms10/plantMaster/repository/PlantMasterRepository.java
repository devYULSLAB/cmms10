package com.cmms10.plantMaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmms10.plantMaster.entity.PlantMaster;
import com.cmms10.plantMaster.entity.PlantMasterIdClass;

import java.util.Optional;

/**
 * cmms10 - PlantMasterRepository
 * 설비 마스터 정보 조회 인터페이스
 * 
 * @author cmms10
 * @since 2024-03-19
 */

@Repository
public interface PlantMasterRepository extends JpaRepository<PlantMaster, PlantMasterIdClass> {

    /**
     * 회사 ID별 최대 plant ID를 조회합니다.
     *
     * @param companyId 회사 ID
     * @return Optional<String> 최대 plant ID
     */
    @Query("SELECT MAX(p.plantId) FROM PlantMaster p WHERE p.companyId = :companyId")
    String findMaxPlantIdByCompanyId(@Param("companyId") String companyId);

    /**
     * 회사 ID별 최대 plant ID를 조회합니다.
     *
     * @param companyId 회사 ID
     * @param siteId 사이트 ID
     * @return Optional<String> 최대 plant ID
     */
    @Query("SELECT MAX(p.plantId) FROM PlantMaster p WHERE p.companyId = :companyId AND p.siteId = :siteId")
    String findMaxPlantIdByCompanyIdAndSiteId(@Param("companyId") String companyId, @Param("siteId") String siteId);

    /**
     * Finds a specific PlantMaster by its companyId and plantId.
     * This is equivalent to JpaRepository's findById method when the IdClass is used.
     *
     * @param companyId The ID of the company.
     * @param plantId The ID of the plant.
     * @return Page 객체로 반환
     */
    Page<PlantMaster> findByCompanyIdAndPlantIdAndDeleteMarkIsNull(String companyId, String plantId, Pageable pageable);

    /**
     * Finds a specific PlantMaster by its companyId and plantId.
     * This is equivalent to JpaRepository's findById method when the IdClass is used.
     *
     * @param companyId The ID of the company.
     * @param siteId The ID of the site.
     * @param plantId The ID of the plant.
     * @return An Optional containing the PlantMaster if found, or empty otherwise.
     */
    Optional<PlantMaster> findByCompanyIdAndSiteIdAndPlantIdAndDeleteMarkIsNull(String companyId, String siteId, String plantId);

    /**
     * Finds a page of PlantMaster entries for a given companyId and siteId.
     *
     * @param companyId The ID of the company.
     * @param siteId The ID of the site.
     * @param pageable Pagination information.
     * @return A page of PlantMaster entities.
     */
    Page<PlantMaster> findByCompanyIdAndSiteIdAndDeleteMarkIsNull(String companyId, String siteId, Pageable pageable);

    /**
     * Finds a page of PlantMaster entries for a given companyId and plantName.
     *
     * @param companyId The ID of the company.
     * @param plantName The name of the plant (partial match).
     * @param pageable Pagination information.
     * @return A page of PlantMaster entities.
     */

    Page<PlantMaster> findByCompanyIdAndPlantNameContainingAndDeleteMarkIsNull(String companyId, String plantName, Pageable pageable);

    /**
     * Finds a page of PlantMaster entries for a given companyId and createBy.
     *
     * @param companyId The ID of the company.
     * @param respDept Responsible department.
     * @param pageable Pagination information.
     * @return A page of PlantMaster entities.
     */
    Page<PlantMaster> findByCompanyIdAndRespDeptAndDeleteMarkIsNull(String companyId, String respDept, Pageable pageable);

}
