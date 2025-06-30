package com.cmms10.inspection.repository; // << UPDATED PACKAGE

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmms10.inspection.entity.InspectionItem;
import com.cmms10.inspection.entity.InspectionItemIdClass;

import java.util.List;
import java.util.Optional;

// Imports for InspectionItem and InspectionItemIdClass are not strictly needed here
// as they are in the same package.

/**
 * cmms10 - InspectionItemRepository
 * 점검 항목 관리 레포지토리
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Repository
public interface InspectionItemRepository extends JpaRepository<InspectionItem, InspectionItemIdClass> {

    /**
     * Finds all InspectionItem entries for a given companyId, inspectionId, and scheduleId,
     * ordered by itemId in ascending order.
     *
     * @param companyId The ID of the company.
     * @param inspectionId The ID of the inspection.
     * @return A list of InspectionItem entities.
     */
    List<InspectionItem> findByCompanyIdAndInspectionIdOrderByItemIdAsc(String companyId, String inspectionId);

    /**
     * Finds a specific InspectionItem by its full composite key.
     *
     * @param companyId The ID of the company.
     * @param inspectionId The ID of the inspection.
     * @param itemId The ID of the item.
     * @return An Optional containing the InspectionItem if found, or empty otherwise.
     */
    Optional<InspectionItem> findByCompanyIdAndInspectionIdAndItemId(String companyId, String inspectionId, String itemId);

    /**
     * Deletes all InspectionItem entries for a given companyId and inspectionId.
     *
     * @param companyId The ID of the company.
     * @param inspectionId The ID of the inspection.
     */
    void deleteByCompanyIdAndInspectionId(String companyId, String inspectionId);

    /**
     * companyId, inspectionId, scheduleId, itemId 로 삭제
     * @param companyId 회사 ID 
     * @param inspectionId 점검 ID
     * @param itemId 항목 ID
     */
    void deleteByCompanyIdAndInspectionIdAndItemId(String companyId, String inspectionId, String itemId);

    /**
     * 회사 ID와 점검 ID로 점검 항목 목록을 조회
     * @param companyId 회사 ID
     * @param inspectionId 점검 ID
     * @return 점검 항목 목록
     */
    List<InspectionItem> findByCompanyIdAndInspectionId(String companyId, String inspectionId);
}
