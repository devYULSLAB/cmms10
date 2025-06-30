package com.cmms10.workorder.repository;

import com.cmms10.workorder.entity.WorkorderItem;
import com.cmms10.workorder.entity.WorkorderItemIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Imports for workorderItem and workorderItemIdClass are not strictly needed here
// as they are in the same package.

@Repository
public interface WorkorderItemRepository extends JpaRepository<WorkorderItem, WorkorderItemIdClass> {

    /**
     * 회사 ID와 작업지시 ID별 최대 항목 ID를 조회합니다.
     *
     * @param companyId 회사 ID
     * @param orderId 작업지시 ID
     * @return Optional<String> 최대 항목 ID
     */
    @Query("SELECT MAX(w.itemId) FROM WorkorderItem w WHERE w.companyId = :companyId AND w.orderId = :orderId")
    String findMaxItemIdByCompanyIdAndOrderId(
        @Param("companyId") String companyId,
        @Param("orderId") String orderId
    );
    
    /**
     * Finds all workorderItem entries for a given companyId and orderId,
     * ordered by itemId in ascending order.
     *
     * @param companyId The ID of the company.
     * @param orderId The ID of the work order.
     * @return A list of workorderItem entities.
     */
    List<WorkorderItem> findByCompanyIdAndOrderIdOrderByItemIdAsc(String companyId, String orderId);

    /**
     * Finds a specific workorderItem by its full composite key.
     *
     * @param companyId The ID of the company.
     * @param orderId The ID of the work order.
     * @param itemId The ID of the item.
     * @return An Optional containing the workorderItem if found, or empty otherwise.
     */
    Optional<WorkorderItem> findByCompanyIdAndOrderIdAndItemId(String companyId, String orderId, String itemId);
}
