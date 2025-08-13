/*
 * 패키지: com.cmms10.checksheet.service
 * 폴더 구조: src/main/java/com/cmms10/checksheet/service/
 * 프로그램명: ChecksheetTemplateService.java
 * 주요 기능: 체크시트 템플릿 비즈니스 로직 처리 (생성, 수정, 삭제, 조회)
 * 생성자: admin
 * 생성일: 2025-01-27
 */

package com.cmms10.checksheet.service;

import com.cmms10.checksheet.entity.ChecksheetTemplate;
import com.cmms10.checksheet.repository.ChecksheetTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChecksheetTemplateService {

    private final ChecksheetTemplateRepository repository;

    public ChecksheetTemplateService(ChecksheetTemplateRepository repository) {
        this.repository = repository;
    }

    public String generateTemplateId() {
        // 'T' + yymmdd + 3자리 랜덤 (예: T240728123)
        String date = new java.text.SimpleDateFormat("yyMMdd").format(new java.util.Date());
        int random = (int) (Math.random() * 1000); // 0~999
        return String.format("T%s%03d", date, random);
    }

    /**
     * 체크시트 템플릿을 저장합니다 (신규/수정 구분).
     * 기능: templateId가 없으면 신규 저장, 있으면 수정 처리합니다.
     * 파라미터:
     * - template: 저장할 템플릿 객체
     * 반환값: 없음
     */
    @Transactional
    public void saveTemplate(ChecksheetTemplate template) {
        boolean isNewChecksheet = template.getTemplateId() == null || template.getTemplateId().trim().isEmpty();
        if (isNewChecksheet) {
            // 신규 저장: templateId 생성, createBy/createDate 설정 후 저장
            template.setTemplateId(generateTemplateId());
            // createBy, createDate는 컨트롤러에서 세팅됨
            repository.save(template);
        } else {
            // 수정: 기존 데이터 조회 후 필요한 필드만 업데이트
            ChecksheetTemplate existingTemplate = repository.findByCompanyIdAndTemplateId(
                    template.getCompanyId(), template.getTemplateId())
                    .orElseThrow(() -> new RuntimeException("수정할 템플릿이 존재하지 않습니다."));
            existingTemplate.setTemplateName(template.getTemplateName());
            existingTemplate.setTemplateJson(template.getTemplateJson());
            // createBy, createDate는 기존 값 유지
            repository.save(existingTemplate);
        }
    }

    @Transactional(readOnly = true)
    public List<ChecksheetTemplate> getTemplatesByCompanyId(String companyId) {
        return repository.findAllByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public ChecksheetTemplate getTemplateByCompanyIdAndTemplateId(String companyId, String templateId) {
        return repository.findByCompanyIdAndTemplateId(companyId, templateId)
                .orElseThrow(() -> new RuntimeException("해당 템플릿이 존재하지 않습니다."));
    }

    @Transactional
    public void deleteTemplate(String companyId, String templateId) {
        repository.deleteByCompanyIdAndTemplateId(companyId, templateId);
    }
}
