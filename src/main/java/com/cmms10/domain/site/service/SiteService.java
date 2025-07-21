package com.cmms10.domain.site.service;

import com.cmms10.domain.site.entity.Site;
import com.cmms10.domain.site.entity.SiteIdClass;
import com.cmms10.domain.site.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * cmms10 - SiteService
 * 사이트 관리 서비스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Service
@Transactional
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    /**
     * 회사 ID로 사이트 목록 조회
     */
    public List<Site> getAllSitesByCompanyId(String companyId) {
        return siteRepository.findByCompanyIdAndDeleteMarkIsNull(companyId);
    }

    /**
     * 사이트 ID로 조회
     */
    public Site getSiteByCompanyIdAndSiteId(String companyId, String siteId) {
        return siteRepository.findByCompanyIdAndSiteIdAndDeleteMarkIsNull(companyId, siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));
    }
    
    /**
     * 사이트 저장
     */
    public Site save(Site site, String username, String mode) {
        LocalDateTime now = LocalDateTime.now();
        if (mode.equals("new")) {
            // 신규 등록 시 ID가 비어있으면 예외 발생
            if (site.getSiteId() == null || site.getSiteId().isEmpty()) {
                throw new RuntimeException("사이트 ID는 필수입니다.");
            }
            // 중복 체크
            boolean isDuplicate = siteRepository.existsById(new SiteIdClass(site.getCompanyId(), site.getSiteId()));
            if (isDuplicate) {
                throw new RuntimeException("삭제되었거나 존재하는 사이트 ID입니다: " + site.getSiteId());
            }
            // 신규 등록 시 사이트 정보를 설정
            site.setCreateDate(now);
            site.setCreateBy(username);

        } else if (mode.equals("edit")) {
            // 수정 모드에서는 siteId가 반드시 있어야 함
            if (site.getSiteId() == null || site.getSiteId().isEmpty()) {
                throw new RuntimeException("사이트 ID는 필수입니다.");
            }
            // 수정 시 사이트 정보를 설정
            site.setUpdateDate(now);    
            site.setUpdateBy(username);
        } 
        
        return siteRepository.save(site);
    }

    /**
     * 사이트 삭제
     */
    public void delete(String companyId, String siteId, String username) {
        LocalDateTime now = LocalDateTime.now();
        siteRepository.findByCompanyIdAndSiteIdAndDeleteMarkIsNull(companyId, siteId).ifPresent(site -> {
                    site.setUpdateBy(username);
                    site.setUpdateDate(now);
                    site.setDeleteMark("Y");
                    siteRepository.save(site);
                });
    }
} 