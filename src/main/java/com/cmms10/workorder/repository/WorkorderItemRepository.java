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
     * CompanyId와 orderId로 workorderItem 엔티티를 페이징하여 조회합니다.
     *
     * @param companyId 회사 ID
     * @param orderId 작업지시 ID
     * @return List<WorkorderItem> 작업지시 항목 엔티티
     */
    List<WorkorderItem> findByCompanyIdAndOrderIdOrderByItemIdAsc(String companyId, String orderId);

    /**
     * CompanyId와 orderId로 workorderItem 엔티티를 조회합니다.
     *
     * @param companyId 회사 ID
     * @param orderId 작업지시 ID
     * @param itemId 항목 ID
     * @return Optional<WorkorderItem> 작업지시 항목 엔티티
     */
    Optional<WorkorderItem> findByCompanyIdAndOrderIdAndItemId(String companyId, String orderId, String itemId);
}
