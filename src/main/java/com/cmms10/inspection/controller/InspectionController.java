package com.cmms10.inspection.controller;

import com.cmms10.inspection.entity.Inspection;
import com.cmms10.inspection.entity.InspectionItem;
import com.cmms10.inspection.service.InspectionService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.commonCode.service.CommonCodeService;
import jakarta.servlet.http.HttpSession;

import java.util.List; // Added import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

/**
 * cmms10 - InspectionController
 * 점검 관리 컨트롤러
 */
@Controller
@RequestMapping("/inspection")
public class InspectionController {

    private final InspectionService inspectionService;
    private final SiteService siteService;
    private final DeptService deptService;
    private final CommonCodeService commonCodeService;

    public InspectionController(InspectionService inspectionService,
            SiteService siteService,
            DeptService deptService,
            CommonCodeService commonCodeService) {
        this.inspectionService = inspectionService;
        this.siteService = siteService;
        this.deptService = deptService;
        this.commonCodeService = commonCodeService;
    }

    /** 신규 폼 */
    @GetMapping("/inspectionForm")
    public String form(Model model, HttpSession session) {
        // 세션에서 기본값 설정
        String companyId = (String) session.getAttribute("companyId");

        Inspection inspection = new Inspection();

        List<InspectionItem> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(new InspectionItem());
        }
        inspection.setCompanyId(companyId);
        inspection.setItems(items);

        // Select box 데이터 추가
        model.addAttribute("inspection", inspection);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("jobTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "JOBTP"));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));

        return "inspection/inspectionForm";
    }

    /** 수정 폼 */
    @GetMapping("/inspectionForm/{siteId}/{inspectionId}")
    public String editForm(@PathVariable String siteId,
            @PathVariable String inspectionId,
            Model model,
            HttpSession session) {

        String companyId = (String) session.getAttribute("companyId");

        Inspection inspection = inspectionService.getInspectionByCompanyIdAndSiteIdAndInspectionId(companyId, siteId,
                inspectionId);
        List<InspectionItem> items = inspectionService.getInspectionItemByCompanyIdAndInspectionId(companyId,
                inspectionId);
        inspection.setItems(items);

        // Select box 데이터 추가
        model.addAttribute("inspection", inspection);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("jobTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "JOBTP"));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));

        return "inspection/inspectionForm";
    }

    /** 저장 */
    @PostMapping("/inspectionSave")
    public String saveInspection(@ModelAttribute Inspection inspection,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");

        inspectionService.saveInspection(inspection, username);
        redirectAttributes.addFlashAttribute("successMessage", "점검이 저장되었습니다.");
        return "redirect:/inspection/inspectionList";
    }

    /** 삭제 */
    @PostMapping("/inspectionDelete/{siteId}/{inspectionId}")
    public String deleteInspection(@PathVariable String siteId,
            @PathVariable String inspectionId,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        try {
            inspectionService.deleteInspection(companyId, siteId, inspectionId);
        } catch (Exception e) {
            throw new RuntimeException("점검 삭제 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/inspection/inspectionList";
    }

    /** 목록 조회 : 검색 조건별 */
    @GetMapping("/inspectionList")
    public String list(@RequestParam(required = false) String plantId,
            @RequestParam(required = false) String siteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            HttpSession session,
            @PageableDefault(size = 10, sort = "inspectionId") Pageable pageable) {
        String companyId = (String) session.getAttribute("companyId");
        // 검색 조건 system.out
        System.out.println("inspectionList search");
        System.out.println("plantId: " + plantId);
        System.out.println("siteId: " + siteId);

        Page<Inspection> inspections;
        if (plantId != null && !plantId.isEmpty() && siteId != null && !siteId.isEmpty()) {
            // 특정 설비의 점검 목록
            inspections = inspectionService.getInspectionByCompanyIdAndSiteIdAndPlantId(companyId, siteId, plantId,
                    pageable);
        } else if (plantId != null && !plantId.isEmpty()) {
            // 특정 설비의 점검 목록
            inspections = inspectionService.getInspectionByCompanyIdAndPlantId(companyId, plantId, pageable);
        } else if (siteId != null && !siteId.isEmpty()) {
            // 특정 사이트의 점검 목록
            inspections = inspectionService.getAllInspectionsByCompanyIdAndSiteId(companyId, siteId, pageable);
        } else {
            // 전체 점검 목록
            inspections = inspectionService.getAllInspectionsByCompanyId(companyId, pageable);
        }

        model.addAttribute("inspections", inspections);
        model.addAttribute("searchPlantId", plantId);
        model.addAttribute("searchSiteId", siteId);
        return "inspection/inspectionList";
    }

    /** 출력(조회) 폼 */
    @GetMapping("/inspectionDetail/{siteId}/{inspectionId}")
    public String detail(@PathVariable String siteId,
            @PathVariable String inspectionId,
            HttpSession session,
            Model model) {
        String companyId = (String) session.getAttribute("companyId");
        Inspection inspection = inspectionService.getInspectionByCompanyIdAndSiteIdAndInspectionId(companyId, siteId,
                inspectionId);
        List<InspectionItem> items = inspectionService.getInspectionItemByCompanyIdAndInspectionId(companyId,
                inspectionId);
        inspection.setItems(items);
        model.addAttribute("inspection", inspection);
        return "inspection/inspectionDetail"; // 출력 전용 템플릿로 변경
    }
}
