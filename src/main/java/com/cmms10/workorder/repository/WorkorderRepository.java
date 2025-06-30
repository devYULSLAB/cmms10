package com.cmms10.workorder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import com.cmms10.workorder.entity.Workorder;
import com.cmms10.workorder.entity.WorkorderIdClass;


// Imports for workorder and workorderIdClass are not strictly needed here
// as they are in the same package.

@Repository
public interface WorkorderRepository extends JpaRepository<Workorder, WorkorderIdClass> {

    /**
     * 회사 ID별 최대 작업지시 ID를 조회합니다.
     *
     * @param companyId 회사 ID
     * @return Optional<String> 최대 작업지시 ID
     */
    @Query("SELECT MAX(w.orderId) FROM Workorder w WHERE w.companyId = :companyId")
    String findMaxOrderIdByCompanyId(@Param("companyId") String companyId);

    /**
     * Finds a page of workorder entries for a given companyId and siteId.
     *
     * @param companyId The ID of the company.
     * @param siteId The ID of the site.
     * @param pageable Pagination information.
     * @return A page of workorder entities.
     */
    Page<Workorder> findByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable);

    /**
     * CompanyId와 plantid로 workorder 엔티티를 페이징하여 조회합니다.
     * @param companyId
     * @param plantId
     * @param pageable
     * @return
     */
    Page<Workorder> findByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);

    /**
     * Finds a specific workorder by its companyId and orderId.
     *
     * @param companyId The ID of the company.
     * @param orderId The ID of the work order.
     * @return An Optional containing the workorder if found, or empty otherwise.
     */
    Optional<Workorder> findByCompanyIdAndOrderId(String companyId, String orderId);


}
