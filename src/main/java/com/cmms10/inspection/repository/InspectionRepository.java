package com.cmms10.inspection.repository; // << UPDATED PACKAGE

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmms10.inspection.entity.Inspection;
import com.cmms10.inspection.entity.InspectionIdClass;

import java.util.Optional;
import jakarta.persistence.LockModeType;

// Imports for Inspection and InspectionIdClass are not strictly needed here
// as they are in the same package.

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, InspectionIdClass> {

        /**
         * 회사 ID별 최대 inspection ID를 조회합니다.
         *
         * @param companyId 회사 ID
         * @return Optional<String> 최대 inspection ID
         */
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT MAX(p.inspectionId) FROM Inspection p WHERE p.companyId = :companyId")
        String findMaxInspectionIdByCompanyId(@Param("companyId") String companyId);

        /**
         * 회사 ID와 점검 ID로 특정 점검을 조회합니다.
         *
         * @param companyId    회사 ID
         * @param siteId       사이트 ID
         * @param inspectionId 점검 ID
         * @return 점검 엔티티 (Optional)
         */
        Optional<Inspection> findByCompanyIdAndSiteIdAndInspectionId(String companyId, String siteId,
                        String inspectionId);

        /**
         * 회사 ID와 사이트 ID로 점검 목록을 페이징하여 조회합니다.
         *
         * @param companyId 회사 ID
         * @param pageable  페이징 정보
         * @return 점검 엔티티 페이지
         */
        Page<Inspection> findByCompanyIdOrderByInspectionIdAsc(String companyId, Pageable pageable);

        /**
         * 회사 ID와 사이트 ID로 점검 목록을 페이징하여 조회합니다.
         *
         * @param companyId 회사 ID
         * @param siteId    사이트 ID
         * @param pageable  페이징 정보
         * @return 점검 엔티티 페이지
         */
        Page<Inspection> findByCompanyIdAndSiteIdOrderByInspectionIdAsc(String companyId, String siteId,
                        Pageable pageable);

        /**
         * 회사 ID와 수행 부서로 점검 목록을 페이징하여 조회합니다.
         *
         * @param companyId 회사 ID
         * @param Dept      수행 부서
         * @param pageable  페이징 정보
         * @return 점검 엔티티 페이지
         */
        Page<Inspection> findByCompanyIdAndDeptIdOrderByInspectionIdAsc(String companyId, String deptId,
                        Pageable pageable);

        /**
         * 회사 ID와 점검 이름으로 점검 목록을 페이징하여 조회합니다.
         *
         * @param companyId      회사 ID
         * @param inspectionName 점검 이름
         * @param pageable       페이징 정보
         * @return 점검 엔티티 페이지
         */
        Page<Inspection> findByCompanyIdAndInspectionNameContainingOrderByInspectionIdAsc(String companyId,
                        String inspectionName, Pageable pageable);

        /**
         * 회사 ID, 사이트 ID, 플랜트 ID로 점검을 조회합니다.
         *
         * @param companyId 회사 ID
         * @param siteId    사이트 ID
         * @param plantId   플랜트 ID
         * @return 점검 엔티티 목록
         */
        Page<Inspection> findByCompanyIdAndSiteIdAndPlantIdOrderByInspectionIdAsc(String companyId, String siteId,
                        String plantId, Pageable pageable);

        /**
         * 회사 ID와 플랜트 ID로 점검을 조회합니다.
         *
         * @param companyId 회사 ID
         * @param plantId   플랜트 ID
         * @return 점검 엔티티 목록
         */
        Page<Inspection> findByCompanyIdAndPlantIdOrderByInspectionIdAsc(String companyId, String plantId,
                        Pageable pageable);

        /**
         * 회사 ID와 점검 ID로 점검을 삭제합니다.
         *
         * @param companyId    회사 ID
         * @param inspectionId 점검 ID
         */
        void deleteByCompanyIdAndSiteIdAndInspectionId(String companyId, String siteId, String inspectionId);
}
