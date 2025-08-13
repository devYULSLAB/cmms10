/*
 * 패키지: com.cmms10.checksheet.controller
 * 폴더 구조: src/main/java/com/cmms10/checksheet/controller/
 * 프로그램명: ChecksheetTemplateController.java
 * 주요 기능: 체크시트 템플릿 관리 (생성, 수정, 삭제, 조회)
 * 생성자: admin
 * 생성일: 2025-01-27
 */

package com.cmms10.checksheet.controller;

import com.cmms10.checksheet.entity.ChecksheetTemplate;
import com.cmms10.checksheet.service.ChecksheetTemplateService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checksheet")
@RequiredArgsConstructor
public class ChecksheetTemplateController {

    private final ChecksheetTemplateService templateService;

    /**
     * 체크시트 템플릿 작성 화면을 표시합니다.
     * 기능: 새로운 템플릿을 작성할 수 있는 폼을 제공합니다.
     * 파라미터:
     * - model: 뷰에 데이터를 전달하기 위한 Model 객체
     * - session: 사용자 세션 정보 (companyId 추출용)
     * 반환값: 템플릿 작성 폼 뷰 페이지
     */
    @GetMapping("/checksheetTemplateForm")
    public String templateForm(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        ChecksheetTemplate checksheetTemplate = new ChecksheetTemplate();
        checksheetTemplate.setCompanyId(companyId);
        model.addAttribute("checksheetTemplate", checksheetTemplate);
        return "checksheet/checksheetTemplateForm";
    }

    /**
     * 체크시트 템플릿 수정 화면을 표시합니다.
     * 기능: 기존 템플릿을 수정할 수 있는 폼을 제공합니다.
     * 파라미터:
     * - templateId: 수정할 템플릿의 ID
     * - model: 뷰에 데이터를 전달하기 위한 Model 객체
     * - session: 사용자 세션 정보 (companyId 추출용)
     * 반환값: 템플릿 수정 폼 뷰 페이지
     */
    @GetMapping("/checksheetTemplateForm/{templateId}")
    public String templateForm(@PathVariable String templateId, Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        ChecksheetTemplate checksheetTemplate = templateService.getTemplateByCompanyIdAndTemplateId(companyId,
                templateId);

        model.addAttribute("checksheetTemplate", checksheetTemplate);
        return "checksheet/checksheetTemplateForm";
    }

    /**
     * 체크시트 템플릿을 저장합니다 (신규/수정 구분).
     * 기능: ChecksheetTemplate 객체를 받아서 신규 저장 또는 수정 처리를 합니다.
     * 파라미터:
     * - template: 저장할 템플릿 객체 (templateId로 신규/수정 구분)
     * - session: 사용자 세션 정보 (companyId, username 추출용)
     * 반환값: 템플릿 목록 페이지로 리다이렉트
     */
    @PostMapping("/checksheetTemplateSave")
    public String saveTemplate(@ModelAttribute ChecksheetTemplate template,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        // 신규 저장인 경우에만 생성자 정보 설정
        if (template.getTemplateId() == null || template.getTemplateId().trim().isEmpty()) {
            template.setCreateBy(username);
            template.setCreateDate(LocalDateTime.now());
        }

        template.setCompanyId(companyId);

        templateService.saveTemplate(template);
        return "redirect:/checksheet/checksheetTemplateList";
    }

    /**
     * 체크시트 템플릿 목록을 조회합니다.
     * 기능: 회사별 템플릿 목록을 조회하여 화면에 표시합니다.
     * 파라미터:
     * - model: 뷰에 데이터를 전달하기 위한 Model 객체
     * - session: 사용자 세션 정보 (companyId 추출용)
     * 반환값: 템플릿 목록 뷰 페이지
     */
    @GetMapping("/checksheetTemplateList")
    public String templateList(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<ChecksheetTemplate> list = templateService.getTemplatesByCompanyId(companyId);
        model.addAttribute("checksheetTemplateList", list);
        return "checksheet/checksheetTemplateList";
    }

    /**
     * 체크시트 템플릿을 삭제합니다.
     * 기능: 지정된 템플릿을 데이터베이스에서 삭제합니다.
     * 파라미터:
     * - templateId: 삭제할 템플릿의 ID
     * - session: 사용자 세션 정보 (companyId 추출용)
     * 반환값: 템플릿 목록 페이지로 리다이렉트
     */
    @PostMapping("/checksheetTemplateDelete/{templateId}")
    public String templateDelete(@PathVariable String templateId, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        try {
            templateService.deleteTemplate(companyId, templateId);
        } catch (Exception e) {
            throw new RuntimeException("템플릿 삭제 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/checksheet/checksheetTemplateList";
    }

    /**
     * 체크시트 템플릿을 JSON 형태로 반환합니다.
     * 기능: Ajax 요청이나 formRender에서 사용할 수 있는 JSON 형태의 템플릿 데이터를 제공합니다.
     * 파라미터:
     * - session: 사용자 세션 정보 (companyId 추출용)
     * 반환값: 템플릿 목록과 JSON 맵을 포함한 Map 객체
     */
    @GetMapping("/checksheetTemplateMap")
    @ResponseBody
    public Map<String, Object> getTemplatesAsMap(HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<ChecksheetTemplate> list = templateService.getTemplatesByCompanyId(companyId);

        Map<String, String> jsonMap = new LinkedHashMap<>();
        for (ChecksheetTemplate tpl : list) {
            jsonMap.put(tpl.getTemplateId(), tpl.getTemplateJson());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("templateList", list);
        result.put("templateJsonMap", jsonMap);
        return result;
    }
}
