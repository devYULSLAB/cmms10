package com.cmms10.commonCode.service;

import com.cmms10.commonCode.entity.CommonCode;
import com.cmms10.commonCode.repository.CommonCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommonCodeService {
    private final CommonCodeRepository commonCodeRepository;

    public CommonCodeService(
            CommonCodeRepository commonCodeRepository) {
        this.commonCodeRepository = commonCodeRepository;
    }

    @Transactional(readOnly = true)
    public List<CommonCode> getAllCommonCodesByCompanyId(String companyId) {
        return commonCodeRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public CommonCode getCommonCodeByCompanyIdAndCodeId(String companyId, String codeId) {
        return commonCodeRepository.findByCompanyIdAndCodeId(companyId, codeId)
                .orElseThrow(() -> new RuntimeException("Common code not found: " + companyId + "/" + codeId));
    }

    @Transactional(readOnly = true)
    public List<CommonCode> getCommonCodesByCompanyIdAndCodeType(String companyId, String codeType) {
        return commonCodeRepository.findByCompanyIdAndCodeType(companyId, codeType);
    }

    @Transactional
    public CommonCode saveCommonCode(CommonCode commonCode) {
        return commonCodeRepository.save(commonCode);
    }

    @Transactional
    public void deleteCommonCode(String companyId, String codeId) {
        commonCodeRepository.deleteByCompanyIdAndCodeId(companyId, codeId);
    }

}