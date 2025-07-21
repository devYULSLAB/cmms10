package com.cmms10.workorder.controller;

import com.cmms10.workorder.entity.Workorder;
import com.cmms10.workorder.entity.WorkorderItem;
import com.cmms10.workorder.service.WorkorderService;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.commonCode.service.CommonCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/workorder")
public class WorkorderController {

    private final WorkorderService workorderService;
    private final DeptService deptService;
    private final CommonCodeService commonCodeService;

    public WorkorderController(WorkorderService workorderService,
                             DeptService deptService,
                             CommonCodeService commonCodeService) {
        this.workorderService = workorderService;
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
        // Select box 데이터 추가
        model.addAttribute("jobTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "JOBTP"));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));
        
        return "workorder/workorderForm";
    }

    /** 수정 폼 */
    @GetMapping("/workorderForm/{orderId}")
    public String editForm(@PathVariable String orderId, Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workorder workorder = workorderService.getWorkorderByCompanyIdAndOrderId(companyId, orderId);
        List<WorkorderItem> items = workorderService.getWorkorderItems(companyId, orderId);
        workorder.setItems(items);
        
        model.addAttribute("workorder", workorder);
        // Select box 데이터 추가
        model.addAttribute("jobTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "JOBTP"));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));
        
        return "workorder/workorderForm";
    }

    @GetMapping("/workorderList")
    public String list(Model model, HttpSession session, Pageable pageable) {
        
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        
        Page<Workorder> workorderPage = workorderService.getAllWorkorders(companyId, siteId, pageable);
        model.addAttribute("workorderPage", workorderPage);
        
        return "workorder/workorderList";
    }    

    @GetMapping("/workorderDetail/{orderId}")
    public String detail(@PathVariable String orderId,
                                    HttpSession session,
                                    Model model) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        Workorder workorder = workorderService.getWorkorderByCompanyIdAndOrderId(companyId, orderId);        
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
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        String username = (String) session.getAttribute("username");

        workorder.setCompanyId(companyId);
        workorder.setSiteId(siteId);

        workorderService.saveWorkorder(workorder, username);

        redirectAttributes.addFlashAttribute("successMessage", "Work order saved successfully");
        return "redirect:/workorder/workorderList";  // Fix redirect path
    }
    
    @PostMapping("/workorderDelete/{orderId}")
    public String delete(@PathVariable String orderId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        try {
            workorderService.deleteWorkorder(companyId, orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Work order deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete work order: " + e.getMessage());
        }
        return "redirect:/workorder/workorderList";  // Fix redirect path
    }

    @PostMapping("/item/workorderItemDelete/{orderId}/{itemId}")
    public String deleteItem(@PathVariable String orderId,
                                    @PathVariable String itemId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        
        String companyId = (String) session.getAttribute("companyId");
        try {
            workorderService.deleteWorkorderItem(companyId, orderId, itemId);
            redirectAttributes.addFlashAttribute("successMessage", "Work order item deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete work order item: " + e.getMessage());
        }
        return "redirect:/workorder/detail/" + orderId;
    }
}