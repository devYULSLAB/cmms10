package com.cmms10.inspection.controller;

import com.cmms10.inspection.entity.Inspection;
import com.cmms10.inspection.entity.InspectionItem;
import com.cmms10.inspection.service.InspectionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List; // Added import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

/**
 * cmms10 - InspectionController
 * 점검 관리 컨트롤러
 */
@Controller
@RequestMapping("/inspection")
public class InspectionController {

    private final InspectionService inspectionService;

    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    /** 신규 폼 */
    @GetMapping("/inspectionForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        String username = (String) session.getAttribute("username");

        Inspection inspection = new Inspection();
        inspection.setCompanyId(companyId);
        inspection.setSiteId(siteId);
        inspection.setCreateBy(username);        

        List<InspectionItem> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            InspectionItem item = new InspectionItem();
            item.setInspection(inspection);
            items.add(item);
        }
        inspection.setItems(items);

        model.addAttribute("inspection", inspection);
        return "inspection/inspectionForm";
    }

    /** 저장 */
    @PostMapping("/inspectionSave")
    public String saveInspection(@Valid @ModelAttribute Inspection inspection,
                                Model model,
                                HttpSession session) {
        String username = (String) session.getAttribute("username");
        inspectionService.saveInspection(inspection, username);
        return "redirect:/inspection/inspectionList";
    }

    /** 삭제 */
    @PostMapping("/inspectionDelete/{inspectionId}")
    public String deleteInspection(@PathVariable String inspectionId, HttpSession session) {
        try {
            String companyId = (String) session.getAttribute("companyId");
            inspectionService.deleteInspection(companyId, inspectionId);
        } catch (Exception e) {
            throw new RuntimeException("점검 삭제 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/inspection/inspectionList";
    }
    
    /** 목록 조회 */
    @GetMapping("/inspectionList")
    public String list(Model model,
                       HttpSession session,
                       @PageableDefault(size = 10, sort = "inspectionId") Pageable pageable) {
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        Page<Inspection> inspectionPage = inspectionService.getAllInspections(companyId, siteId, pageable);
        model.addAttribute("inspectionPage", inspectionPage);
        //model.addAttribute("companyId", companyId);
        //model.addAttribute("siteId", siteId);
        return "inspection/inspectionList";
    }

    /** 수정 폼 */
    @GetMapping("/inspectionDetail/{inspectionId}")
    public String detail(@PathVariable String inspectionId,
                         HttpSession session,
                         Model model) {
        String companyId = (String) session.getAttribute("companyId");
        Optional<Inspection> inspectionOpt = inspectionService.getInspectionByInspectionId(companyId, inspectionId);
        if (inspectionOpt.isPresent()) {
            Inspection inspection = inspectionOpt.get();
            // InspectionItem 리스트 조회 및 세팅
            List<InspectionItem> items = inspectionService.getInspectionItemByInspectionId(companyId, inspectionId);
            inspection.setItems(items);

            model.addAttribute("inspection", inspection);
            return "inspection/inspectionForm";
        }
        return "redirect:/inspection/inspectionList";
    }
}
