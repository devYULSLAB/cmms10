package com.cmms10.domain.site.repository;

import com.cmms10.domain.site.entity.Site;
import com.cmms10.domain.site.entity.SiteIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * cmms10 - SiteRepository
 * 사이트 관리 레포지토리
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, SiteIdClass> {
    
    /**
     * 회사 ID, 사이트 ID와 삭제 여부로 사이트 정보를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param siteId 사이트 ID
     * @return 사이트 정보
     */
    Optional<Site> findByCompanyIdAndSiteIdAndDeleteMarkIsNull(String companyId, String siteId);

    /**
     * 회사 ID로 삭제되지 않은 사이트 목록을 조회합니다.
     * 
     * @param companyId 회사 ID
     * @return 사이트 목록
     */
    List<Site> findByCompanyIdAndDeleteMarkIsNull(String companyId);
}
