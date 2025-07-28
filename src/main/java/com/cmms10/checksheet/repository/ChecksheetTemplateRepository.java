package com.cmms10.checksheet.repository;

import com.cmms10.checksheet.entity.ChecksheetTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChecksheetTemplateRepository extends JpaRepository<ChecksheetTemplate, String> {
    List<ChecksheetTemplate> findAllByCompanyId(String companyId);
}
