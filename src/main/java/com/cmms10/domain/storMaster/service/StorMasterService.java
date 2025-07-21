package com.cmms10.domain.storMaster.service;

import com.cmms10.domain.storMaster.entity.StorMaster;
import com.cmms10.domain.storMaster.entity.StorMasterIdClass;
import com.cmms10.domain.storMaster.repository.StorMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StorMasterService {
    private final StorMasterRepository storMasterRepository;

    public StorMasterService(StorMasterRepository storMasterRepository) {
        this.storMasterRepository = storMasterRepository;
    }

    public List<StorMaster> findByCompanyId(String companyId) {
        return storMasterRepository.findByCompanyId(companyId);
    }

    public List<StorMaster> findByCompanyIdAndSiteId(String companyId, String siteId) {
        return storMasterRepository.findByCompanyIdAndSiteId(companyId, siteId);
    }

    public StorMaster findByCompanyIdAndSiteIdAndLocId(String companyId, String siteId, String locId) {
        return storMasterRepository.findByCompanyIdAndSiteIdAndLocId(companyId, siteId, locId)
        .orElseThrow(() -> new RuntimeException("창고 마스터를 찾을 수 없습니다: " + companyId + "/" + siteId + "/" + locId));
    }

    public List<StorMaster> findByCompanyIdAndSiteIdAndParentLocId(String companyId, String siteId, String parentLocId) {
        return storMasterRepository.findByCompanyIdAndSiteIdAndParentLocId(companyId, siteId, parentLocId);
    }

    public List<StorMaster> findByCompanyIdAndSiteIdAndLocNameContaining(String companyId, String siteId, String locName) {
        return storMasterRepository.findByCompanyIdAndSiteIdAndLocNameContaining(companyId, siteId, locName);
    }

    public StorMaster save(StorMaster storMaster) {
        return storMasterRepository.save(storMaster);
    }

    public void deleteById(StorMasterIdClass id) {
        storMasterRepository.deleteById(id);
    }
} 