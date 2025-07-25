package com.cmms10.domain.dept.service;

import com.cmms10.domain.dept.entity.Dept;
import com.cmms10.domain.dept.entity.DeptIdClass;
import com.cmms10.domain.dept.repository.DeptRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * cmms10 - DeptService
 * 부서 정보를 관리하는 서비스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Service
@Transactional
public class DeptService {

    private final DeptRepository deptRepository;

    public DeptService(DeptRepository deptRepository) {
        this.deptRepository = deptRepository;
    }

    @Transactional(readOnly = true)
    public java.util.List<Dept> getAllDeptsByCompanyId(String companyId) {
        return deptRepository.findByCompanyIdAndDeleteMarkIsNull(companyId);
    }

    /**
     * 회사 ID와 부서 ID로 부서 정보를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param deptId    부서 ID
     * @return 부서 정보
     */
    @Transactional(readOnly = true)
    public Dept getDeptByCompanyIdAndDeptId(String companyId, String deptId) {
        return deptRepository.findByCompanyIdAndDeptIdAndDeleteMarkIsNull(companyId, deptId)
                .orElseThrow(() -> new RuntimeException("Dept not found: " + companyId + "/" + deptId));
    }

    /**
     * 부서 정보를 저장합니다.
     * 
     * @param dept 부서 정보
     * @return 저장된 부서 정보
     */
    public Dept saveDept(Dept dept, String username, String mode) {
        LocalDateTime now = LocalDateTime.now();

        if (mode.equals("new")) {
            // deptId가 null이거나 empty
            if (dept.getDeptId() == null || dept.getDeptId().isEmpty()) {
                throw new RuntimeException("부서 ID는 필수 입력 항목입니다.");
            }
            // 중복 체크
            DeptIdClass deptId = new DeptIdClass(dept.getCompanyId(), dept.getDeptId());
            boolean isDuplicate = deptRepository.existsById(deptId);
            if (isDuplicate) {
                throw new RuntimeException("삭제되었거나 존재하는 부서 ID입니다: " + dept.getDeptId());
            }
            // 신규 등록
            dept.setCreateDate(now);
            dept.setCreateBy(username);
        } else if (mode.equals("edit")) {
            // deptId가 null이거나 empty
            if (dept.getDeptId() == null || dept.getDeptId().isEmpty()) {
                throw new RuntimeException("부서 ID는 필수 입력 항목입니다.");
            }
            // 수정 시
            dept.setUpdateDate(now);
            dept.setUpdateBy(username);
        }
        return deptRepository.save(dept);
    }

    /**
     * 부서 정보를 삭제합니다.
     * 
     * @param companyId 회사 ID
     * @param deptId    부서 ID
     */
    public void deleteDept(String companyId, String deptId, String username) {
        deptRepository.findByCompanyIdAndDeptIdAndDeleteMarkIsNull(companyId, deptId)
                .ifPresent(dept -> {
                    dept.setUpdateBy(username);
                    dept.setUpdateDate(java.time.LocalDateTime.now());
                    dept.setDeleteMark("Y");
                    deptRepository.save(dept);
                });
    }
}