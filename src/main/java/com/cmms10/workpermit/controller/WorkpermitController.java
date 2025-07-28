package com.cmms10.workpermit.controller;

import com.cmms10.workpermit.entity.Workpermit;
import com.cmms10.workpermit.entity.WorkpermitItem;
import com.cmms10.workpermit.service.WorkpermitService;
import com.cmms10.domain.site.service.SiteService;

import com.cmms10.checksheet.entity.ChecksheetTemplate;
import com.cmms10.checksheet.entity.ChecksheetResult;
import com.cmms10.checksheet.service.ChecksheetTemplateService;
import com.cmms10.checksheet.repository.ChecksheetResultRepository;

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
    private final ChecksheetTemplateService checksheetTemplateService;
    private final ChecksheetResultRepository checksheetResultRepository;

    // 작업허가 신규 등록
    @GetMapping("/workpermitForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workpermit workpermit = new Workpermit();
        List<WorkpermitItem> items = new ArrayList<>();
        items.add(new WorkpermitItem());
        workpermit.setItems(items);

        List<ChecksheetTemplate> templateList = checksheetTemplateService.getTemplatesByCompany(companyId);
        Map<String, String> templateJsonMap = new LinkedHashMap<>();
        for (ChecksheetTemplate tpl : templateList) {
            templateJsonMap.put(tpl.getTemplateId(), tpl.getTemplateJson());
        }

        model.addAttribute("workpermit", workpermit);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
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

        List<ChecksheetTemplate> templateList = checksheetTemplateService.getTemplatesByCompany(companyId);
        Map<String, String> templateJsonMap = new LinkedHashMap<>();
        for (ChecksheetTemplate tpl : templateList) {
            templateJsonMap.put(tpl.getTemplateId(), tpl.getTemplateJson());
        }

        model.addAttribute("workpermit", permit);
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
        if (templateId != null && checkResultJson != null && !checkResultJson.isBlank()) {
            ChecksheetResult result = new ChecksheetResult();
            result.setCompanyId(companyId);
            result.setPermitId(workpermit.getPermitId());
            result.setTemplateId(templateId);
            result.setCheckResultJson(checkResultJson);
            result.setCreateBy(username);
            result.setCreateDate(LocalDateTime.now());
            checksheetResultRepository.save(result);
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
        ChecksheetResult result = checksheetResultRepository.findByCompanyIdAndPermitId(companyId, permitId);
        model.addAttribute("checkResultJson", result != null ? result.getCheckResultJson() : "{}");

        return "workpermit/workpermitDetail";
    }
}
