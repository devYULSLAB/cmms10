package com.cmms10.commonCode.service;

import com.cmms10.commonCode.entity.CommonCode;
import com.cmms10.commonCode.entity.CommonCodeItem;
import com.cmms10.commonCode.repository.CommonCodeRepository;
import com.cmms10.commonCode.repository.CommonCodeItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommonCodeService {
    private final CommonCodeRepository commonCodeRepository;
    private final CommonCodeItemRepository commonCodeItemRepository;

    public CommonCodeService(
            CommonCodeRepository commonCodeRepository,
            CommonCodeItemRepository commonCodeItemRepository) {
        this.commonCodeRepository = commonCodeRepository;
        this.commonCodeItemRepository = commonCodeItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CommonCode> getAllCommonCodes(String companyId) {
        return commonCodeRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Optional<CommonCode> getCommonCode(String companyId, String codeId) {
        return commonCodeRepository.findByCompanyIdAndCodeId(companyId, codeId);
    }

    @Transactional(readOnly = true)
    public List<CommonCode> getCommonCodesByCodeType(String companyId, String codeType) {
        return commonCodeRepository.findByCompanyIdAndCodeType(companyId, codeType);
    }

    @Transactional(readOnly = true)
    public List<CommonCodeItem> getCommonCodeItems(String companyId, String codeId) {
        return commonCodeItemRepository.findByCompanyIdAndCodeIdOrderByCodeItemIdAsc(companyId, codeId);
    }

    @Transactional(readOnly = true)
    public Optional<CommonCodeItem> getCommonCodeItem(String companyId, String codeId, String codeItemId) {
        return commonCodeItemRepository.findByCompanyIdAndCodeIdAndCodeItemId(companyId, codeId, codeItemId);
    }

    @Transactional
    public CommonCode saveCommonCode(CommonCode commonCode) {
        return commonCodeRepository.save(commonCode);
    }

    @Transactional
    public CommonCodeItem saveCommonCodeItem(CommonCodeItem codeItem) {
        return commonCodeItemRepository.save(codeItem);
    }

    @Transactional
    public void deleteCommonCode(CommonCode commonCode) {
        commonCodeRepository.delete(commonCode);
    }

    @Transactional
    public void deleteCommonCodeItem(CommonCodeItem commonCodeItem) {
        commonCodeItemRepository.delete(commonCodeItem);
    }
}