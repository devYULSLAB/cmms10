package com.cmms10.storMaster.repository;

import com.cmms10.storMaster.entity.StorMaster;
import com.cmms10.storMaster.entity.StorMasterIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StorMasterRepository extends JpaRepository<StorMaster, StorMasterIdClass> {
    /*
     * 회사 ID로 창고 마스터 조회
     */
    List<StorMaster> findByCompanyId(String companyId);

    /*
     * 회사 ID와 사이트 ID로 창고 마스터 조회
     */
    List<StorMaster> findByCompanyIdAndSiteId(String companyId, String siteId);

    /*
     * 회사 ID와 사이트 ID와 창고 ID로 창고 마스터 조회
     */
    Optional<StorMaster> findByCompanyIdAndSiteIdAndLocId(String companyId, String siteId, String locId);

    /*
     * 회사 ID와 사이트 ID와 상위 창고 ID로 창고 마스터 조회
     */
    List<StorMaster> findByCompanyIdAndSiteIdAndParentLocId(String companyId, String siteId, String parentLocId);

    /*
     * 회사 ID와 사이트 ID와 창고 이름을 포함하여 창고 마스터 조회
     */
    List<StorMaster> findByCompanyIdAndSiteIdAndLocNameContaining(String companyId, String siteId, String locName);
}