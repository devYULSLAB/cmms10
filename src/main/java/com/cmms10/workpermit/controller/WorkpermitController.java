package com.cmms10.workpermit.controller;

import com.cmms10.workpermit.entity.Workpermit;
import com.cmms10.workpermit.entity.WorkpermitItem;
import com.cmms10.workpermit.service.WorkpermitService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.commonCode.service.CommonCodeService;

import com.cmms10.checksheet.entity.ChecksheetTemplate;
import com.cmms10.checksheet.entity.ChecksheetResult;
import com.cmms10.checksheet.service.ChecksheetTemplateService;
import com.cmms10.checksheet.service.ChecksheetResultService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/workpermit")
@RequiredArgsConstructor
public class WorkpermitController {

    private final WorkpermitService workpermitService;
    private final SiteService siteService;
    private final CommonCodeService commonCodeService;
    private final DeptService deptService;
    private final ChecksheetTemplateService checksheetTemplateService;
    private final ChecksheetResultService checksheetResultService;

    // 작업허가 신규 등록
    @GetMapping("/workpermitForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workpermit workpermit = new Workpermit();
        List<WorkpermitItem> items = new ArrayList<>();
        items.add(new WorkpermitItem());
        workpermit.setItems(items);

        List<ChecksheetTemplate> templateList = checksheetTemplateService.getTemplatesByCompanyId(companyId);
        Map<String, String> templateJsonMap = new LinkedHashMap<>();
        for (ChecksheetTemplate tpl : templateList) {
            templateJsonMap.put(tpl.getTemplateId(), tpl.getTemplateJson());
        }

        model.addAttribute("workpermit", workpermit);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("permitTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "PERMT")); // 허가서
                                                                                                                       // 유형
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId)); // 부서
        model.addAttribute("templateList", templateList);
        model.addAttribute("templateJsonMap", templateJsonMap);

        return "workpermit/workpermitForm";
    }

    // 수정용 폼
    @GetMapping("/workpermitForm/{siteId}/{permitId}")
    public String editForm(@PathVariable String siteId,
            @PathVariable String permitId,
            Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workpermit permit = workpermitService.getWorkpermitByCompanyIdAndPermitId(companyId, siteId, permitId);
        List<WorkpermitItem> items = workpermitService.getWorkpermitItems(companyId, permitId);
        permit.setItems(items);

        List<ChecksheetTemplate> templateList = checksheetTemplateService.getTemplatesByCompanyId(companyId);
        Map<String, String> templateJsonMap = new LinkedHashMap<>();
        for (ChecksheetTemplate tpl : templateList) {
            templateJsonMap.put(tpl.getTemplateId(), tpl.getTemplateJson());
        }

        model.addAttribute("workpermit", permit);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("permitTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "PERMT"));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));
        model.addAttribute("templateList", templateList);
        model.addAttribute("templateJsonMap", templateJsonMap);

        return "workpermit/workpermitForm";
    }

    // 저장 처리
    @PostMapping("/workpermitSave")
    public String save(@ModelAttribute Workpermit workpermit,
            @RequestParam(required = false) String templateId,
            @RequestParam(required = false) String checkResultJson,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        workpermit.setCompanyId(companyId);
        workpermitService.saveWorkpermit(workpermit, username);

        // 체크리스트 결과 저장
        System.out.println("=== 체크시트 결과 저장 시작 ===");
        System.out.println("templateId: " + templateId);
        System.out.println("checkResultJson 길이: " + (checkResultJson != null ? checkResultJson.length() : 0));
        System.out.println("checkResultJson 값: " + checkResultJson);

        // templateId가 있으면 체크시트 결과 저장
        if (templateId != null && !templateId.trim().isEmpty()) {
            try {
                System.out.println("체크시트 결과 저장 시작");

                // checkResultJson이 null이거나 빈 문자열이면 빈 객체로 설정
                String finalCheckResultJson = checkResultJson;
                if (finalCheckResultJson == null || finalCheckResultJson.trim().isEmpty()) {
                    finalCheckResultJson = "{}";
                    System.out.println("체크시트 결과가 비어있어 빈 객체로 설정");
                }

                System.out.println("체크시트 결과 샘플: "
                        + finalCheckResultJson.substring(0, Math.min(200, finalCheckResultJson.length())) + "...");

                // 기존 결과가 있는지 확인
                ChecksheetResult result;
                try {
                    result = checksheetResultService.getResultByCompanyIdAndPermitId(companyId,
                            workpermit.getPermitId());
                    System.out.println("기존 체크시트 결과 발견 - 업데이트");
                } catch (RuntimeException e) {
                    result = new ChecksheetResult();
                    result.setCompanyId(companyId);
                    result.setPermitId(workpermit.getPermitId());
                    result.setCreateBy(username);
                    result.setCreateDate(LocalDateTime.now());
                    System.out.println("새 체크시트 결과 생성");
                }

                result.setTemplateId(templateId);
                result.setCheckResultJson(finalCheckResultJson);

                checksheetResultService.saveResult(result);
                System.out.println("체크시트 결과 저장 완료 - permitId: " + workpermit.getPermitId());

            } catch (Exception e) {
                System.err.println("체크시트 결과 저장 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // templateId가 없어도 빈 객체로 저장 (기록 유지)
            try {
                System.out.println("템플릿이 선택되지 않음 - 빈 체크시트 결과 저장");

                ChecksheetResult result;
                try {
                    result = checksheetResultService.getResultByCompanyIdAndPermitId(companyId,
                            workpermit.getPermitId());
                    System.out.println("기존 체크시트 결과 발견 - 빈 객체로 업데이트");
                } catch (RuntimeException e) {
                    result = new ChecksheetResult();
                    result.setCompanyId(companyId);
                    result.setPermitId(workpermit.getPermitId());
                    result.setCreateBy(username);
                    result.setCreateDate(LocalDateTime.now());
                    System.out.println("새 체크시트 결과 생성 (빈 객체)");
                }

                result.setTemplateId(""); // 빈 템플릿 ID
                result.setCheckResultJson("{}"); // 빈 JSON 객체

                checksheetResultService.saveResult(result);
                System.out.println("빈 체크시트 결과 저장 완료 - permitId: " + workpermit.getPermitId());

            } catch (Exception e) {
                System.err.println("빈 체크시트 결과 저장 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }

        redirectAttributes.addFlashAttribute("successMessage", "Work permit saved successfully");
        return "redirect:/workpermit/workpermitList";
    }

    // 삭제
    @PostMapping("/workpermitDelete/{siteId}/{permitId}")
    public String delete(@PathVariable String siteId, @PathVariable String permitId, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        workpermitService.deleteWorkpermit(companyId, permitId);
        return "redirect:/workpermit/workpermitList";
    }

    // 목록
    @GetMapping("/workpermitList")
    public String list(Model model, HttpSession session, Pageable pageable) {
        String companyId = (String) session.getAttribute("companyId");
        Page<Workpermit> workpermits = workpermitService.getAllWorkpermitsByCompanyId(companyId, pageable);
        model.addAttribute("workpermits", workpermits);
        return "workpermit/workpermitList";
    }

    // 상세 조회
    @GetMapping("/workpermitDetail/{siteId}/{permitId}")
    public String detail(@PathVariable String siteId,
            @PathVariable String permitId,
            Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workpermit workpermit = workpermitService.getWorkpermitByCompanyIdAndPermitId(companyId, siteId, permitId);
        List<WorkpermitItem> items = workpermitService.getWorkpermitItems(companyId, permitId);
        workpermit.setItems(items);
        model.addAttribute("workpermit", workpermit);

        // 체크시트 결과 포함
        try {
            ChecksheetResult result = checksheetResultService.getResultByCompanyIdAndPermitId(companyId, permitId);
            String checkResultJson = result != null ? result.getCheckResultJson() : "{}";
            System.out.println("체크시트 결과 JSON: " + checkResultJson);
            model.addAttribute("checkResultJson", checkResultJson);
        } catch (RuntimeException e) {
            System.out.println("체크시트 결과 없음: " + e.getMessage());
            model.addAttribute("checkResultJson", "{}");
        }

        return "workpermit/workpermitDetail";
    }
}
