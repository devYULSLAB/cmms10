package com.cmms10.domain.dept.repository;

import com.cmms10.domain.dept.entity.Dept;
import com.cmms10.domain.dept.entity.DeptIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * cmms10 - DeptRepository
 * 부서 정보를 관리하는 Repository
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Repository
public interface DeptRepository extends JpaRepository<Dept, DeptIdClass> {
    
    /**
     * 회사 ID로 전체 부서 정보를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @return 부서 정보 목록
     */
    List<Dept> findByCompanyIdAndDeleteMarkIsNull(String companyId);

    /**
     * 회사 ID, 부서 ID와 삭제 여부로 부서 정보를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param deptId 부서 ID
     * @return 부서 정보
     */
    Optional<Dept> findByCompanyIdAndDeptIdAndDeleteMarkIsNull(String companyId, String deptId);
}
