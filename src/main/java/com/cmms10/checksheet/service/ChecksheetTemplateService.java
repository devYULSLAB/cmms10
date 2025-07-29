package com.cmms10.checksheet.service;

import com.cmms10.checksheet.entity.ChecksheetTemplate;
import com.cmms10.checksheet.repository.ChecksheetTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChecksheetTemplateService {

    private final ChecksheetTemplateRepository repository;

    public ChecksheetTemplateService(ChecksheetTemplateRepository repository) {
        this.repository = repository;
    }

    public String generateTemplateId() {
        // 'T' + yymmdd + 3자리 랜덤 (예: T240728123)
        String date = new java.text.SimpleDateFormat("yyMMdd").format(new java.util.Date());
        int random = (int) (Math.random() * 1000); // 0~999
        return String.format("T%s%03d", date, random);
    }

    @Transactional
    public void saveTemplate(ChecksheetTemplate template) {
        repository.save(template);
    }

    @Transactional(readOnly = true)
    public List<ChecksheetTemplate> getTemplatesByCompanyId(String companyId) {
        return repository.findAllByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public ChecksheetTemplate getTemplateByCompanyIdAndTemplateId(String companyId, String templateId) {
        return repository.findByCompanyIdAndTemplateId(companyId, templateId)
                .orElseThrow(() -> new RuntimeException("해당 템플릿이 존재하지 않습니다."));
    }

    @Transactional
    public void deleteTemplate(String companyId, String templateId) {
        repository.deleteByCompanyIdAndTemplateId(companyId, templateId);
    }
}
