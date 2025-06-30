package com.cmms10.commonCode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmms10.commonCode.entity.CommonCodeItem;
import com.cmms10.commonCode.entity.CommonCodeItemIdClass;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommonCodeItemRepository extends JpaRepository<CommonCodeItem, CommonCodeItemIdClass> {

    /**
     * Finds all CommonCodeItem entries for a given companyId and codeId.
     * This is useful for fetching all items belonging to a specific common code.
     *
     * @param companyId The ID of the company.
     * @param codeId The ID of the common code.
     * @return A list of CommonCodeItem entities, ordered by codeItemId by default if not specified otherwise.
     */
    List<CommonCodeItem> findByCompanyIdAndCodeIdOrderByCodeItemIdAsc(String companyId, String codeId);

    /**
     * Finds a specific CommonCodeItem by its full composite key.
     * JpaRepository's findById can also be used, but this provides a more explicit method name.
     *
     * @param companyId The ID of the company.
     * @param codeId The ID of the common code.
     * @param codeItemId The ID of the common code item.
     * @return An Optional containing the CommonCodeItem if found, or empty otherwise.
     */
    Optional<CommonCodeItem> findByCompanyIdAndCodeIdAndCodeItemId(String companyId, String codeId, String codeItemId);
}
