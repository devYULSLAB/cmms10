package com.cmms10.funcMaster.repository;

import com.cmms10.funcMaster.entity.FuncMaster;
import com.cmms10.funcMaster.entity.FuncMasterIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 프로그램명: CMMS10
 * 패키지명: com.cmms10.funcMaster.repository
 * 클래스명: FuncMasterRepository
 * 주요기능: FuncMaster 엔티티의 데이터베이스 접근을 위한 리포지토리
 * 생성자: AI Assistant
 * 생성일: 2024-12-19
 */
@Repository
public interface FuncMasterRepository extends JpaRepository<FuncMaster, FuncMasterIdClass> {

    /**
     * 회사 ID로 모든 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @return 기능 마스터 목록
     */
    List<FuncMaster> findByCompanyId(String companyId);

    /**
     * 회사 ID로 모든 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @return 기능 마스터 목록
     */
    List<FuncMaster> findByCompanyIdAndSiteId(String companyId, String siteId);

    /**
     * 회사 ID와 기능 ID로 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @param funcId    기능 ID
     * @return 기능 마스터 (Optional)
     */
    Optional<FuncMaster> findByCompanyIdAndSiteIdAndFuncId(String companyId, String siteId, String funcId);

    /**
     * 회사 ID와 기능 타입으로 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @param funcType  기능 타입
     * @return 기능 마스터 목록
     */
    List<FuncMaster> findByCompanyIdAndSiteIdAndFuncType(String companyId, String siteId, String funcType);

    /**
     * 회사 ID와 기능 이름으로 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @param funcName  기능 이름
     * @return 기능 마스터 목록
     */
    List<FuncMaster> findByCompanyIdAndSiteIdAndFuncNameContaining(String companyId, String siteId, String funcName);

    /**
     * 회사 ID로 기능 마스터 개수 조회
     * 
     * @param companyId 회사 ID
     * @return 기능 마스터 개수
     */
    long countByCompanyId(String companyId);

    /**
     * 회사 ID와 기능 ID로 기능 마스터 존재 여부 확인
     * 
     * @param companyId 회사 ID
     * @param funcId    기능 ID
     * @return 존재 여부
     */
    boolean existsByCompanyIdAndSiteIdAndFuncId(String companyId, String siteId, String funcId);
}