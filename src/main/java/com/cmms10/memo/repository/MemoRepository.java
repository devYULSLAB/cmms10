package com.cmms10.memo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmms10.memo.entity.Memo;
import com.cmms10.memo.entity.MemoIdClass;

import java.util.Optional;


/**
 * cmms10 - MemoRepository
 * 메모 레포지토리
 * 
 * @author cmms10
 * @since 2024-03-19
 */

@Repository
public interface MemoRepository extends JpaRepository<Memo, MemoIdClass> {

    /**
     * 회사 ID별 최대 메모 ID를 조회합니다.
     *
     * @param companyId 회사 ID
     * @return 최대 메모 ID (없으면 null)
     */
    @Query("SELECT MAX(m.memoId) FROM Memo m WHERE m.companyId = :companyId")
    String findMaxMemoIdByCompanyId(@Param("companyId") String companyId);

    /**
     * 특정 메모를 조회합니다. (삭제되지 않은 메모만)
     *
     * @param companyId 회사 ID
     * @param memoId 메모 ID
     * @param deleteMark 삭제 여부 (삭제되지 않은 메모만 조회)
     * @return Optional<Memo> 메모 정보
     */
    Optional<Memo> findByCompanyIdAndMemoId(
        String companyId, 
        String memoId
    );

    /**
     * 회사 ID, 사이트 ID로 삭제되지 않은 메모 목록을 페이지네이션하여 조회합니다.
     *
     * @param companyId 회사 ID
     * @param siteId 사이트 ID
     * @param pageable 페이지 정보
     * @return Page<Memo> 메모 페이지
     */
    Page<Memo> findByCompanyIdAndSiteId(
        String companyId, 
        String siteId, 
        Pageable pageable
    );

    /**
     * 회사 ID와 메모 이름으로 삭제되지 않은 메모 목록을 페이지네이션하여 조회합니다.
     *
     * @param companyId 회사 ID
     * @param memoName 메모 이름 (부분 일치)
     * @param pageable 페이지 정보
     * @return Page<Memo> 메모 페이지
     */

    Page<Memo> findByCompanyIdAndMemoNameContaining(
        String companyId, 
        String memoName, 
        Pageable pageable
    );

    /**
     * 회사 ID와 생성자자로 삭제되지 않은 메모를 조회합니다.
     *
     * @param companyId 회사 ID
     * @param createBy 메모 생성자
     * @return Optional<Memo> 메모 정보
     */
    Page<Memo> findByCompanyIdAndCreateBy(
        String companyId, 
        String createBy,
        Pageable pageable       
    );
}
