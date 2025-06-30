package com.cmms10.domain.site.service;

import com.cmms10.domain.site.entity.Site;
import com.cmms10.domain.site.entity.SiteIdClass;
import com.cmms10.domain.site.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private SiteRepository siteRepository;

    /**
     * 회사 ID로 사이트 목록 조회
     */
    public List<Site> findByCompanyId(String companyId) {
        return siteRepository.findByCompanyIdAndDeleteMarkIsNull(companyId);
    }

    /**
     * 사이트 ID로 조회
     */
    public Site findById(String companyId, String siteId) {
        return siteRepository.findByCompanyIdAndSiteIdAndDeleteMarkIsNull(companyId, siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));
    }

    /**
     * 사이트 저장
     */
    public Site save(Site site) {
        return siteRepository.save(site);
    }

    /**
     * 사이트 삭제
     */
    public void delete(String companyId, String siteId) {
        siteRepository.findByCompanyIdAndSiteIdAndDeleteMarkIsNull(companyId, siteId)
                .ifPresent(site -> {
                    site.setDeleteMark("Y");
                    siteRepository.save(site);
                });
    }
} 