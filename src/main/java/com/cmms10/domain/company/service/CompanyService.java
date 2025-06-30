package com.cmms10.domain.company.service;

import com.cmms10.domain.company.entity.Company;
import com.cmms10.domain.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public java.util.List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    /**
     * 회사 ID로 회사 정보를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @return 회사 정보
     */
    @Transactional(readOnly = true)
    public Optional<Company> getCompanyById(String companyId) {
        return companyRepository.findByCompanyIdAndDeleteMarkIsNull(companyId);
    }

    /**
     * 회사 정보를 저장합니다.
     * 
     * @param company 회사 정보
     * @return 저장된 회사 정보
     */
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    /**
     * 회사 정보를 삭제합니다.
     * 
     * @param companyId 회사 ID
     */
    public void deleteCompany(String companyId) {
        companyRepository.findByCompanyIdAndDeleteMarkIsNull(companyId).ifPresent(company -> {
            company.setDeleteMark("Y");
            companyRepository.save(company);
        });
    }
} 