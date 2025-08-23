package com.cmms10.checksheet.controller;

import com.cmms10.checksheet.entity.ChecksheetResult;
import com.cmms10.checksheet.entity.ChecksheetTemplate;
import com.cmms10.checksheet.service.ChecksheetResultService;
import com.cmms10.checksheet.service.ChecksheetTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

/**
 * 패키지: com.cmms10.checksheet.controller
 * 클래스: ChecksheetResultController
 * 주요기능: 체크시트 결과 관련 컨트롤러
 * 생성자: (로그인 이름 입력)
 * 생성일: (생성일 입력)
 */
@Controller
@RequestMapping("/checksheet")
public class ChecksheetResultController {

    private final ChecksheetResultService checksheetResultService;
    private final ChecksheetTemplateService checksheetTemplateService;

    public ChecksheetResultController(ChecksheetResultService checksheetResultService,
            ChecksheetTemplateService checksheetTemplateService) {
        this.checksheetResultService = checksheetResultService;
        this.checksheetTemplateService = checksheetTemplateService;
    }

    /**
     * 체크시트 결과 상세 페이지를 표시합니다.
     * 
     * @param permitId 허가서 ID
     * @param model    모델 객체
     * @param session  세션 객체
     * @return 체크시트 결과 상세 페이지
     */
    @GetMapping("/checksheetResultDetail/{permitId}")
    public String checksheetResultDetail(@PathVariable String permitId,
            Model model,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        try {
            ChecksheetResult result = checksheetResultService.getResultByCompanyIdAndPermitId(companyId, permitId);
            model.addAttribute("checksheetResult", result);

            // 템플릿 JSON 추가
            if (result != null && result.getTemplateId() != null) {
                try {
                    ChecksheetTemplate template = checksheetTemplateService.getTemplateByCompanyIdAndTemplateId(
                            companyId, result.getTemplateId());
                    if (template != null) {
                        model.addAttribute("templateJson", template.getTemplateJson());
                    }
                } catch (Exception e) {
                    // 템플릿 조회 실패 시 무시
                    model.addAttribute("templateJson", null);
                }
            }

            return "workpermit/checksheetResultDetail";
        } catch (RuntimeException e) {
            // 결과가 없는 경우 빈 모델로 페이지 표시
            model.addAttribute("checksheetResult", null);
            model.addAttribute("templateJson", null);
            return "workpermit/checksheetResultDetail";
        }
    }
}