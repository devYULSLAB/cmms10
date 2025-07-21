package com.cmms10.workpermit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmms10.workpermit.entity.WorkpermitItem;
import com.cmms10.workpermit.entity.WorkpermitItemIdClass;


/**
 * 패키지명: com.cmms10.workPermit.repository
 * 파일명: WorkPermitItemRepository.java
 * 프로그램명: 작업허가 항목 JPA 리포지토리
 * 생성자: cmms10
 * 생성일: 2024-03-19
 */
@Repository
public interface WorkpermitItemRepository extends JpaRepository<WorkpermitItem, WorkpermitItemIdClass> {
    /**
     * 회사 ID와 작업허가 ID별 최대 항목 ID를 조회합니다.
     *
     * @param companyId 회사 ID
     * @param permitId 작업허가 ID
     * @return Optional<String> 최대 항목 ID
     */
    @Query("SELECT MAX(w.itemId) FROM WorkpermitItem w WHERE w.companyId = :companyId AND w.permitId = :permitId")
    String findMaxItemIdByCompanyIdAndPermitId(@Param("companyId") String companyId, @Param("permitId") String permitId);

    /**
     * CompanyId와 permitId로 workpermitItem 엔티티를 페이징하여 조회합니다.
     * @param companyId
     * @param permitId
     * @param pageable
     * @return
     */
    List<WorkpermitItem> findByCompanyIdAndPermitId(String companyId, String permitId);

    /**
     * CompanyId와 permitId로 workpermitItem 엔티티를 조회합니다.
     * @param companyId
     * @param permitId
     * @return Optional<WorkpermitItem> 작업허가 항목 엔티티
     */
    Optional<WorkpermitItem> findByCompanyIdAndPermitIdAndItemIdOrderByItemIdAsc(String companyId, String permitId, String itemId);
} 