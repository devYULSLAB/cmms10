package com.cmms10.workpermit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.cmms10.workpermit.entity.Workpermit;
import com.cmms10.workpermit.entity.WorkpermitIdClass;

/**
 * 패키지명: com.cmms10.workPermit.repository
 * 파일명: WorkPermitRepository.java
 * 프로그램명: 작업허가서 JPA 리포지토리
 * 생성자: cmms10
 * 생성일: 2024-03-19
 */
@Repository
public interface WorkpermitRepository extends JpaRepository<Workpermit, WorkpermitIdClass> {
    /**
     * 회사 ID별 최대 작업허가가 ID를 조회합니다.
     *
     * @param companyId 회사 ID
     * @return Optional<String> 최대 작업허가 ID
     *         workpermitItem에서 siteId는 PK가 아니므로 workpermit가 unique하려면 permitId는
     *         companyId의 MAX값이어야 합니다.(companyId,siteId의 Max이면 안됩니다.)
     */
    @Query("SELECT MAX(w.permitId) FROM Workpermit w WHERE w.companyId = :companyId")
    String findMaxPermitIdByCompanyId(@Param("companyId") String companyId);

    /**
     * CompanyId로 workpermit 엔티티를 페이징하여 조회합니다.
     * 
     * @param companyId
     * @param pageable
     * @return
     */
    Page<Workpermit> findByCompanyIdOrderByPermitIdAsc(String companyId, Pageable pageable);

    /**
     * CompanyId와 siteId로 workpermit 엔티티를 페이징하여 조회합니다.
     * 
     * @param companyId
     * @param siteId
     * @param pageable
     * @return
     */
    Page<Workpermit> findByCompanyIdAndSiteIdOrderByPermitIdAsc(String companyId, String siteId, Pageable pageable);

    /**
     * CompanyId와 plantId로 workpermit 엔티티를 페이징하여 조회합니다.
     * 
     * @param companyId
     * @param plantId
     * @param pageable
     * @return
     */
    Page<Workpermit> findByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);

    /**
     * CompanyId와 permitName을 포함하여 페이징하여 조회합니다.
     * 
     * @param companyId
     * @param permitName
     * @param pageable
     * @return
     */
    Page<Workpermit> findByCompanyIdAndPermitNameContaining(String companyId, String permitName, Pageable pageable);

    /**
     * CompanyId와 permitId로 workpermit 엔티티를 조회합니다.
     *
     * @param companyId 회사 ID
     * @param permitId  작업허가 ID
     * @return Optional<Workpermit> 작업허가 엔티티
     */
    Optional<Workpermit> findByCompanyIdAndPermitId(String companyId, String permitId);
}
