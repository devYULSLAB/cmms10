package com.cmms10.workorder.controller;

import com.cmms10.workorder.entity.Workorder;
import com.cmms10.workorder.entity.WorkorderItem;
import com.cmms10.workorder.service.WorkorderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/workorder")
public class WorkorderController {

    private final WorkorderService workorderService;

    public WorkorderController(WorkorderService workorderService) {
        this.workorderService = workorderService;
    }

    @GetMapping("/workorderList")
    public String list(Model model,
                               HttpSession session,
                               @PageableDefault(size = 10, sort = "orderId") Pageable pageable) {
        
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        
        Page<Workorder> workorderPage = workorderService.getAllWorkorders(companyId, siteId, pageable);
        model.addAttribute("workorderPage", workorderPage);
        
        return "workorder/workorderList";
    }

    @GetMapping("/workorderForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");

        Workorder workorder = new Workorder();
        workorder.setCompanyId(companyId);
        workorder.setSiteId(siteId);

        // 최소 1개 항목 생성 (초기화용)
        WorkorderItem item = new WorkorderItem();
        item.setCompanyId(companyId);
        workorder.getItems().add(item);

        model.addAttribute("workorder", workorder);
        return "workorder/workorderForm";
    }


    @GetMapping("/workorderDetail/{orderId}")
    public String detail(@PathVariable String orderId,
                                    HttpSession session,
                                    Model model) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        workorderService.getWorkorderByWorkorderId(companyId, orderId)
            .ifPresent(workorder -> {
                model.addAttribute("workorder", workorder);
                List<WorkorderItem> items = workorderService.getWorkorderItems(companyId, orderId);
                model.addAttribute("workorderItems", items);
            });
        
        return "workorder/workorderDetail";
    }

    @PostMapping("/workorderSave")
    public String save(@ModelAttribute Workorder workorder,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String username = (String) session.getAttribute("username");

        try {
            workorderService.saveWorkorder(workorder, username);
            redirectAttributes.addFlashAttribute("successMessage", "Work order saved successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save work order: " + e.getMessage());
        }
        return "redirect:/workorder/workorderList";  // Fix redirect path
    }

    @PostMapping("/item/workorderItemSave")
    public String saveItem(@ModelAttribute WorkorderItem workorderItem,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        
        // 세션에서 사용자 정보 가져오기
        String username = (String) session.getAttribute("username");
        try {
            workorderService.saveWorkorderItem(workorderItem, username);
            redirectAttributes.addFlashAttribute("successMessage", "Work order item saved successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save work order item: " + e.getMessage());
        }
        return "redirect:/workorder/detail/" + workorderItem.getOrderId();
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