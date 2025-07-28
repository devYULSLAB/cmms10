package com.cmms10.storMaster.service;

import com.cmms10.storMaster.entity.StorMaster;
import com.cmms10.storMaster.entity.StorMasterIdClass;
import com.cmms10.storMaster.repository.StorMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(
                        () -> new RuntimeException("창고 마스터를 찾을 수 없습니다: " + companyId + "/" + siteId + "/" + locId));
    }

    public List<StorMaster> findByCompanyIdAndSiteIdAndParentLocId(String companyId, String siteId,
            String parentLocId) {
        return storMasterRepository.findByCompanyIdAndSiteIdAndParentLocId(companyId, siteId, parentLocId);
    }

    public List<StorMaster> findByCompanyIdAndSiteIdAndLocNameContaining(String companyId, String siteId,
            String locName) {
        return storMasterRepository.findByCompanyIdAndSiteIdAndLocNameContaining(companyId, siteId, locName);
    }

    public StorMaster saveStorMaster(StorMaster storMaster) {
        return storMasterRepository.save(storMaster);
    }

    public void deleteStorMaster(String companyId, String siteId, String locId) {
        StorMasterIdClass id = new StorMasterIdClass(companyId, siteId, locId);
        storMasterRepository.deleteById(id);
    }
}