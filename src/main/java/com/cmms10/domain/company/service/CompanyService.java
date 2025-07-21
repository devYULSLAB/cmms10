package com.cmms10.domain.company.service;

import com.cmms10.domain.company.entity.Company;
import com.cmms10.domain.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * cmms10 - CompanyService
 * 회사 정보를 관리하는 서비스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAllByDeleteMarkIsNull();
    }

    /**
     * 회사 ID로 회사 정보를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @return 회사 정보
     */
    @Transactional(readOnly = true)
    public Company getCompanyById(String companyId) {
        return companyRepository.findByCompanyIdAndDeleteMarkIsNull(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
    }

    /**
     * 회사 정보를 저장합니다.
     * 
     * @param company 회사 정보
     * @return 저장된 회사 정보
     */
    @Transactional
    public void saveCompany(Company company, String username, String mode) {
        LocalDateTime now = LocalDateTime.now();
        if (mode.equals("new")) {
            // 신규 등록 시 ID가 비어있으면 예외 발생
            if (company.getCompanyId() == null || company.getCompanyId().isEmpty()) {
                throw new RuntimeException("회사 ID는 필수입니다.");
            }
            // 중복 체크 
            boolean isDuplicate = companyRepository.existsById(company.getCompanyId());
            if (isDuplicate) {
                throw new RuntimeException("삭제되었거나 존재하는 회사 ID입니다: " + company.getCompanyId());
            }
            // 신규 등록 시 회사 정보를 설정
            company.setCreateDate(now);
            company.setCreateBy(username);
        } else if (mode.equals("edit")) {
            // 수정 시 ID가 비어있으면 예외 발생
            if (company.getCompanyId() == null || company.getCompanyId().isEmpty()) {
                throw new RuntimeException("수정할 회사 ID는 필수입니다.");
            }
            // 수정 시 회사 정보를 설정
            company.setUpdateDate(now);
            company.setUpdateBy(username);
        }

        companyRepository.save(company);
    }

    /**
     * 회사 정보를 삭제합니다.
     * 
     * @param companyId 회사 ID
     */
    public void deleteCompany(String companyId, String username) {
        LocalDateTime now = LocalDateTime.now();
        companyRepository.findByCompanyIdAndDeleteMarkIsNull(companyId).ifPresent(company -> {
            company.setUpdateDate(now);
            company.setUpdateBy(username);
            company.setDeleteMark("Y");
            companyRepository.save(company);
        });
    }
}