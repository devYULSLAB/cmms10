package com.cmms10.workpermit.controller;

import com.cmms10.workpermit.entity.Workpermit;
import com.cmms10.workpermit.entity.WorkpermitItem;
import com.cmms10.workpermit.service.WorkpermitService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.common.code.service.CommonCodeService;

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

    // 작업허가 신규 등록
    @GetMapping("/workpermitForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workpermit workpermit = new Workpermit();
        List<WorkpermitItem> items = new ArrayList<>();
        items.add(new WorkpermitItem());
        // 회사 ID 설정
        workpermit.setCompanyId(companyId);
        workpermit.setItems(items);

        model.addAttribute("workpermit", workpermit);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("permitTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "PERMT")); // 허가서
                                                                                                                       // 유형
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId)); // 부서

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

        model.addAttribute("workpermit", permit);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("permitTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "PERMT"));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));

        return "workpermit/workpermitForm";
    }

    // 저장 처리
    @PostMapping("/workpermitSave")
    public String save(
            @ModelAttribute("workpermit") Workpermit workpermit,
            @RequestParam(value = "templateId", required = false) String templateId,
            @RequestParam(value = "checkResultJson", required = false) String checkResultJson,
            RedirectAttributes ra,
            HttpSession session) {
        // 1) 작업허가 저장 (신규면 permitId 생성)
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");
        workpermit.setCompanyId(companyId);
        Workpermit saved = workpermitService.saveWorkpermit(workpermit, username);

        ra.addFlashAttribute("successMessage", "저장되었습니다.");
        return "redirect:/workpermit/workpermitForm?permitId=" + saved.getPermitId();
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

        return "workpermit/workpermitDetail";
    }
}
