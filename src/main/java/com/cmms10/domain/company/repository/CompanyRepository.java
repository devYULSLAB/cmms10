package com.cmms10.domain.company.repository;

import com.cmms10.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * cmms10 - CompanyRepository
 * 회사 정보를 관리하는 Repository
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    
    /**
     * 회사 ID와 삭제 여부로 회사 정보를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @return 회사 정보
     */
    Optional<Company> findByCompanyIdAndDeleteMarkIsNull(String companyId);

    /**
     * 삭제되지 않은 모든 회사 정보를 조회합니다.
     * 
     * @return 회사 정보 리스트
     */
    List<Company> findAllByDeleteMarkIsNull();
}
