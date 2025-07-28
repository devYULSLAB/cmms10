package com.cmms10.checksheet.service;

import com.cmms10.checksheet.entity.ChecksheetResult;
import com.cmms10.checksheet.repository.ChecksheetResultRepository;
import org.springframework.stereotype.Service;

@Service
public class ChecksheetResultService {

    private final ChecksheetResultRepository repository;

    public ChecksheetResultService(ChecksheetResultRepository repository) {
        this.repository = repository;
    }

    public void saveResult(ChecksheetResult result) {
        repository.save(result);
    }

    public ChecksheetResult getResult(String companyId, String permitId) {
        return repository.findByCompanyIdAndPermitId(companyId, permitId);
    }
}
