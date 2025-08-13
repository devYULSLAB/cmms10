/*
 * 패키지: com.cmms10.checksheet.service
 * 폴더 구조: src/main/java/com/cmms10/checksheet/service/
 * 프로그램명: ChecksheetResultService.java
 * 주요 기능: 체크시트 결과 저장 및 조회 서비스
 * 생성자: admin
 * 생성일: 2025-01-27
 */

package com.cmms10.checksheet.service;

import com.cmms10.checksheet.entity.ChecksheetResult;
import com.cmms10.checksheet.repository.ChecksheetResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecksheetResultService {

    private final ChecksheetResultRepository repository;

    public ChecksheetResultService(ChecksheetResultRepository repository) {
        this.repository = repository;
    }

    /**
     * 체크시트 결과를 저장합니다.
     * 기능: 체크시트 결과를 데이터베이스에 저장합니다.
     * 파라미터:
     * - result: 저장할 체크시트 결과 객체
     * 반환값: 없음
     */
    @Transactional
    public void saveResult(ChecksheetResult result) {
        System.out.println("=== ChecksheetResult 저장 시작 ===");
        System.out.println("companyId: " + result.getCompanyId());
        System.out.println("permitId: " + result.getPermitId());
        System.out.println("templateId: " + result.getTemplateId());
        System.out.println("checkResultJson 길이: "
                + (result.getCheckResultJson() != null ? result.getCheckResultJson().length() : 0));

        // 데이터 검증
        if (result.getCompanyId() == null || result.getCompanyId().trim().isEmpty()) {
            throw new IllegalArgumentException("companyId는 필수입니다.");
        }
        if (result.getPermitId() == null || result.getPermitId().trim().isEmpty()) {
            throw new IllegalArgumentException("permitId는 필수입니다.");
        }
        if (result.getTemplateId() == null || result.getTemplateId().trim().isEmpty()) {
            throw new IllegalArgumentException("templateId는 필수입니다.");
        }
        if (result.getCheckResultJson() == null) {
            result.setCheckResultJson("{}");
            System.out.println("checkResultJson이 null이어서 빈 객체로 설정");
        }

        try {
            repository.save(result);
            System.out.println("체크시트 결과 저장 성공");
        } catch (Exception e) {
            System.err.println("체크시트 결과 저장 실패: " + e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public ChecksheetResult getResultByCompanyIdAndPermitId(String companyId, String permitId) {
        return repository.findByCompanyIdAndPermitId(companyId, permitId)
                .orElseThrow(() -> new RuntimeException("ChecksheetResult not found"));
    }
}
