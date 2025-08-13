package com.cmms10.workorder.controller;

import com.cmms10.workorder.entity.Workorder;
import com.cmms10.workorder.entity.WorkorderItem;
import com.cmms10.workorder.service.WorkorderService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.commonCode.service.CommonCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/workorder")
public class WorkorderController {

    private final WorkorderService workorderService;
    private final SiteService siteService;
    private final DeptService deptService;
    private final CommonCodeService commonCodeService;

    public WorkorderController(WorkorderService workorderService,
            SiteService siteService,
            DeptService deptService,
            CommonCodeService commonCodeService) {
        this.workorderService = workorderService;
        this.siteService = siteService;
        this.deptService = deptService;
        this.commonCodeService = commonCodeService;
    }

    /** 신규 폼 */
    @GetMapping("/workorderForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workorder workorder = new Workorder();

        // 최소 1개 항목 생성 (초기화용)
        List<WorkorderItem> items = new ArrayList<>();
        items.add(new WorkorderItem());
        workorder.setItems(items);

        model.addAttribute("workorder", workorder);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("JOBTPpes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "JOBTP"));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));

        return "workorder/workorderForm";
    }

    /** 수정 폼 */
    @GetMapping("/workorderForm/{siteId}/{orderId}")
    public String editForm(@PathVariable String siteId, @PathVariable String orderId,
            Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workorder workorder = workorderService.getWorkorderByCompanyIdAndSiteIdAndOrderId(companyId, siteId, orderId);
        List<WorkorderItem> items = workorderService.getWorkorderItems(companyId, orderId);
        workorder.setItems(items);

        model.addAttribute("workorder", workorder);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("JOBTPpes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "JOBTP"));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));

        return "workorder/workorderForm";
    }

    @GetMapping("/workorderList")
    public String list(@RequestParam(required = false) String siteId,
            @RequestParam(required = false) String plantId,
            @PageableDefault(size = 10, sort = "orderId") Pageable pageable,
            Model model, HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        Page<Workorder> workorders;

        if (plantId != null && !plantId.isEmpty() && siteId != null && !siteId.isEmpty()) {
            // 특정 설비의 작업지시 목록
            workorders = workorderService.getWorkordersByCompanyIdAndSiteIdAndPlantId(companyId, siteId, plantId,
                    pageable);
        } else if (plantId != null && !plantId.isEmpty()) {
            // 특정 설비의 작업지시 목록
            workorders = workorderService.getWorkordersByCompanyIdAndPlantId(companyId, plantId, pageable);
        } else if (siteId != null && !siteId.isEmpty()) {
            // 특정 사이트의 작업지시 목록
            workorders = workorderService.getWorkordersByCompanyIdAndSiteId(companyId, siteId, pageable);
        } else {
            // 전체 작업지시 목록
            workorders = workorderService.getAllWorkordersByCompanyId(companyId, pageable);
        }

        model.addAttribute("workorders", workorders);
        model.addAttribute("searchSiteId", siteId);
        model.addAttribute("searchPlantId", plantId);

        return "workorder/workorderList";
    }

    @GetMapping("/workorderDetail/{siteId}/{orderId}")
    public String detail(@PathVariable String siteId, @PathVariable String orderId,
            HttpSession session,
            Model model) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        Workorder workorder = workorderService.getWorkorderByCompanyIdAndSiteIdAndOrderId(companyId, siteId, orderId);
        List<WorkorderItem> items = workorderService.getWorkorderItems(companyId, orderId);
        workorder.setItems(items);
        model.addAttribute("workorder", workorder);

        return "workorder/workorderDetail";
    }

    @PostMapping("/workorderSave")
    public String save(@ModelAttribute Workorder workorder,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String username = (String) session.getAttribute("username");

        workorderService.saveWorkorder(workorder, username);

        redirectAttributes.addFlashAttribute("successMessage", "Work order saved successfully");
        return "redirect:/workorder/workorderList"; // Fix redirect path
    }

    @PostMapping("/workorderDelete/{siteId}/{orderId}")
    public String delete(@PathVariable String siteId, @PathVariable String orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        try {
            workorderService.deleteWorkorder(companyId, siteId, orderId);
        } catch (Exception e) {
            throw new RuntimeException("점검 삭제 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/workorder/workorderList"; // Fix redirect path
    }

}