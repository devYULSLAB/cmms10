package com.cmms10.checksheet.service;

import com.cmms10.checksheet.entity.ChecksheetTemplate;
import com.cmms10.checksheet.repository.ChecksheetTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecksheetTemplateService {

    private final ChecksheetTemplateRepository repository;

    public ChecksheetTemplateService(ChecksheetTemplateRepository repository) {
        this.repository = repository;
    }

    public String generateTemplateId() {
        return "TPL" + System.currentTimeMillis(); // 예: TPL1721234567890
    }

    public void saveTemplate(ChecksheetTemplate template) {
        repository.save(template);
    }

    public List<ChecksheetTemplate> getTemplatesByCompany(String companyId) {
        return repository.findAllByCompanyId(companyId);
    }

    public ChecksheetTemplate getTemplate(String templateId) {
        return repository.findById(templateId).orElse(null);
    }
}
