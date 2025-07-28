package com.cmms10.funcMaster.service;

import com.cmms10.funcMaster.entity.FuncMaster;
import com.cmms10.funcMaster.repository.FuncMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 프로그램명: CMMS10
 * 패키지명: com.cmms10.funcMaster.service
 * 클래스명: FuncMasterService
 * 주요기능: FuncMaster 엔티티의 비즈니스 로직 처리
 * 생성자: AI Assistant
 * 생성일: 2024-12-19
 */
@Service
public class FuncMasterService {

    private final FuncMasterRepository funcMasterRepository;

    public FuncMasterService(FuncMasterRepository funcMasterRepository) {
        this.funcMasterRepository = funcMasterRepository;
    }

    /**
     * 모든 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @return 기능 마스터 목록
     */
    public List<FuncMaster> getAllFuncMastersByCompanyId(String companyId) {
        return funcMasterRepository.findByCompanyId(companyId);
    }

    /**
     * 모든 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @return 기능 마스터 목록
     */
    public List<FuncMaster> getAllFuncMastersByCompanyIdAndSiteId(String companyId, String siteId) {
        return funcMasterRepository.findByCompanyIdAndSiteId(companyId, siteId);
    }

    /**
     * 회사 ID와 기능 ID로 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @param funcId    기능 ID
     * @return 기능 마스터
     */
    public FuncMaster getFuncMasterByCompanyIdAndSiteIdAndFuncId(String companyId, String siteId, String funcId) {
        Optional<FuncMaster> funcMaster = funcMasterRepository.findByCompanyIdAndSiteIdAndFuncId(companyId, siteId,
                funcId);
        return funcMaster.orElse(null);
    }

    /**
     * 기능 마스터 저장
     * 
     * @param funcMaster 저장할 기능 마스터
     * @return 저장된 기능 마스터
     */
    public FuncMaster saveFuncMaster(FuncMaster funcMaster) {
        return funcMasterRepository.save(funcMaster);
    }

    /**
     * 기능 마스터 삭제
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @param funcId    기능 ID
     */
    public void deleteFuncMaster(String companyId, String siteId, String funcId) {
        Optional<FuncMaster> funcMaster = funcMasterRepository.findByCompanyIdAndSiteIdAndFuncId(companyId, siteId,
                funcId);
        funcMaster.ifPresent(funcMasterRepository::delete);
    }

    /**
     * 회사 ID와 기능 타입으로 기능 마스터 조회
     * 
     * @param companyId 회사 ID
     * @param funcType  기능 타입
     * @return 기능 마스터 목록
     */
    public List<FuncMaster> getFuncMastersByCompanyIdAndFuncType(String companyId, String siteId, String funcType) {
        return funcMasterRepository.findByCompanyIdAndSiteIdAndFuncType(companyId, siteId, funcType);
    }

    /**
     * 회사 ID와 기능 이름으로 기능 마스터 검색
     * 
     * @param companyId 회사 ID
     * @param funcName  기능 이름
     * @return 기능 마스터 목록
     */
    public List<FuncMaster> searchFuncMastersByFuncName(String companyId, String siteId, String funcName) {
        return funcMasterRepository.findByCompanyIdAndSiteIdAndFuncNameContaining(companyId, siteId, funcName);
    }

    /**
     * 회사 ID로 기능 마스터 개수 조회
     * 
     * @param companyId 회사 ID
     * @return 기능 마스터 개수
     */
    public long countFuncMastersByCompanyId(String companyId) {
        return funcMasterRepository.countByCompanyId(companyId);
    }

    /**
     * 회사 ID와 기능 ID로 기능 마스터 존재 여부 확인
     * 
     * @param companyId 회사 ID
     * @param funcId    기능 ID
     * @return 존재 여부
     */
    public boolean existsFuncMaster(String companyId, String siteId, String funcId) {
        return funcMasterRepository.existsByCompanyIdAndSiteIdAndFuncId(companyId, siteId, funcId);
    }
}