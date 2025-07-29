package com.cmms10.checksheet.service;

import com.cmms10.checksheet.entity.ChecksheetResult;
import com.cmms10.checksheet.repository.ChecksheetResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecksheetResultService {

    private final ChecksheetResultRepository repository;

    public ChecksheetResultService(ChecksheetResultRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveResult(ChecksheetResult result) {
        repository.save(result);
    }

    @Transactional(readOnly = true)
    public ChecksheetResult getResultByCompanyIdAndPermitId(String companyId, String permitId) {
        return repository.findByCompanyIdAndPermitId(companyId, permitId)
                .orElseThrow(() -> new RuntimeException("ChecksheetResult not found"));
    }
}
