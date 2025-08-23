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

import java.time.LocalDateTime;
import java.util.Optional;

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
        // 데이터 검증
        if (result.getCompanyId() == null || result.getCompanyId().trim().isEmpty()) {
            throw new IllegalArgumentException("companyId는 필수입니다.");
        }
        if (result.getPermitId() == null || result.getPermitId().trim().isEmpty()) {
            throw new IllegalArgumentException("permitId는 필수입니다.");
        }
        // templateId가 비어있으면 빈 문자열로 설정 (필수가 아님)
        if (result.getTemplateId() == null) {
            result.setTemplateId("");
        }
        if (result.getCheckResultJson() == null) {
            result.setCheckResultJson("{}");
        }

        try {
            repository.save(result);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public ChecksheetResult getResultByCompanyIdAndPermitId(String companyId, String permitId) {
        return repository.findByCompanyIdAndPermitId(companyId, permitId)
                .orElseThrow(() -> new RuntimeException("ChecksheetResult not found"));
    }

    /**
     * 체크시트 결과를 upsert합니다.
     * 기능: 기존 결과가 있으면 업데이트, 없으면 새로 생성합니다.
     * 파라미터:
     * - companyId: 회사 ID
     * - permitId: 허가서 ID
     * - templateId: 템플릿 ID
     * - resultJson: 결과 JSON 문자열
     * - userId: 사용자 ID
     * 반환값: 없음
     */
    @Transactional
    public void upsert(String companyId, String permitId, String templateId, String resultJson, String userId) {
        System.out.println("ChecksheetResult upsert 시작 - companyId: " + companyId + ", permitId: " + permitId
                + ", templateId: " + templateId);
        System.out.println("resultJson 길이: " + (resultJson != null ? resultJson.length() : "null"));

        Optional<ChecksheetResult> opt = repository.findByCompanyIdAndPermitId(companyId, permitId);
        ChecksheetResult entity = opt.orElseGet(() -> {
            System.out.println("새로운 ChecksheetResult 생성");
            ChecksheetResult r = new ChecksheetResult();
            r.setCompanyId(companyId);
            r.setPermitId(permitId);
            r.setTemplateId(templateId);
            r.setCreateBy(userId);
            r.setCreateDate(LocalDateTime.now());
            return r;
        });

        if (opt.isPresent()) {
            System.out.println("기존 ChecksheetResult 업데이트");
        }

        entity.setTemplateId(templateId); // 템플릿 교체 가능
        entity.setCheckResultJson(resultJson); // TEXT/CLOB

        try {
            ChecksheetResult saved = repository.save(entity);
            System.out.println("ChecksheetResult 저장 완료 - ID: " + saved.getCompanyId() + "-" + saved.getPermitId());
        } catch (Exception e) {
            System.err.println("ChecksheetResult 저장 실패: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
