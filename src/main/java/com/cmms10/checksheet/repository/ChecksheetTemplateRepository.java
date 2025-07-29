package com.cmms10.checksheet.repository;

import com.cmms10.checksheet.entity.ChecksheetTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChecksheetTemplateRepository extends JpaRepository<ChecksheetTemplate, String> {
    List<ChecksheetTemplate> findAllByCompanyId(String companyId);

    /**
     * 회사 ID와 템플릿릿 ID로 기능 마스터 조회
     * 
     * @param companyId  회사 ID
     * @param templateId 템블릿 ID
     * @return 템플릿마스터터 (Optional)
     */
    Optional<ChecksheetTemplate> findByCompanyIdAndTemplateId(String companyId, String templateId);

    /**
     * 회사 ID와 템플릿 ID로 템플릿을 삭제합니다.
     * 
     * @param companyId  회사 ID
     * @param templateId 템플릿 ID
     */
    void deleteByCompanyIdAndTemplateId(String companyId, String templateId);
}
