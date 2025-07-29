package com.cmms10.checksheet.repository;

import com.cmms10.checksheet.entity.ChecksheetResult;
import com.cmms10.checksheet.entity.ChecksheetResultIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChecksheetResultRepository extends JpaRepository<ChecksheetResult, ChecksheetResultIdClass> {
    Optional<ChecksheetResult> findByCompanyIdAndPermitId(String companyId, String permitId);
}
