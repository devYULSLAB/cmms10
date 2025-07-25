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
     *         workorderItem에서 siteId는 PK가 아니므로 workorder가 unique하려면 orderId는
     *         companyId의 MAX값이어야 합니다.(companyId,siteId의 Max이면 안됩니다.)
     */
    @Query("SELECT MAX(w.orderId) FROM Workorder w WHERE w.companyId = :companyId")
    String findMaxOrderIdByCompanyId(@Param("companyId") String companyId);

    /**
     * CompanyId와 siteId로 workorder 엔티티를 페이징하여 조회합니다.
     *
     * @param companyId The ID of the company.
     * @param pageable  Pagination information.
     * @return A page of workorder entities.
     */
    Page<Workorder> findByCompanyIdOrderByOrderIdAsc(String companyId, Pageable pageable);

    /**
     * CompanyId와 siteId로 workorder 엔티티를 조회합니다.
     *
     * @param companyId The ID of the company.
     * @param siteId    The ID of the site.
     * @param pageable  Pagination information.
     * @return A page of workorder entities.
     */
    Page<Workorder> findByCompanyIdAndSiteIdOrderByOrderIdAsc(String companyId, String siteId, Pageable pageable);

    /**
     * CompanyId와 plantid로 workorder 엔티티를 페이징하여 조회합니다.
     * 
     * @param companyId
     * @param plantId
     * @param pageable
     * @return
     */
    Page<Workorder> findByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);

    /**
     * CompanyId,siteId와 orderId로 workorder 엔티티를 조회합니다.
     *
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @param orderId   작업지시 ID
     * @return Optional<Workorder> 작업지시 엔티티
     */
    Optional<Workorder> findByCompanyIdAndSiteIdAndOrderId(String companyId, String siteId, String orderId);

}
