package com.cmms10.workpermit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmms10.workpermit.entity.Workpermit;
import com.cmms10.workpermit.entity.WorkpermitItem;
import com.cmms10.workpermit.service.WorkpermitService;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/workpermit")
public class WorkpermitController {

    private final WorkpermitService workpermitService;

    public WorkpermitController(WorkpermitService workpermitService) {
        this.workpermitService = workpermitService;
    }

    @GetMapping("/workpermitForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");

        Workpermit workpermit = new Workpermit();
        // item 생성(초기화용 )
        List<WorkpermitItem> items = new ArrayList<>();
        items.add(new WorkpermitItem());
        workpermit.setItems(items);

        model.addAttribute("workpermit", workpermit);
        return "workpermit/workpermitForm";
    }

    @GetMapping("/workpermitForm/{siteId}/{permitId}")
    public String editForm(@PathVariable String siteId, 
                            @PathVariable String permitId, 
                            Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        // 복수의 사이트를 운영하는 경우 List에서 site가 다를 수 있어서 pathvariable 사용함 

        Workpermit permit = workpermitService.getWorkpermitByCompanyIdAndPermitId(companyId, siteId, permitId);
        List<WorkpermitItem> items = workpermitService.getWorkpermitItems(companyId, permitId);
        permit.setItems(items);

        model.addAttribute("workpermit", permit);
        return "workpermit/workpermitForm";
    }

    @PostMapping("/workpermitSave")
    public String save(@ModelAttribute Workpermit workpermit,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        workpermitService.saveWorkpermit(workpermit, (String) session.getAttribute("username"));
        redirectAttributes.addFlashAttribute("successMessage", "Work permit saved successfully");
        return "redirect:/workpermit/workpermitList";
    }

    @PostMapping("/workpermitDelete/{siteId}/{permitId}")
    public String delete(@PathVariable String siteId, @PathVariable String permitId, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        workpermitService.deleteWorkpermit(companyId, permitId);
        return "redirect:/workpermit/workpermitList";
    }

    @GetMapping("/workpermitList")
    public String list(Model model, HttpSession session, Pageable pageable) {
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");

        Page<Workpermit> workpermitPage = workpermitService.getAllWorkpermits(companyId, siteId, pageable);
        model.addAttribute("workpermitPage", workpermitPage);
        return "workPermitList";
    }

    @GetMapping("/workpermitDetail/{siteId}/{permitId}")
    public String detail(@PathVariable String siteId, @PathVariable String permitId, Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Workpermit workpermit = workpermitService.getWorkpermitByCompanyIdAndPermitId(companyId, siteId, permitId);
        List<WorkpermitItem> workpermitItems = workpermitService.getWorkpermitItems(companyId, permitId);
        model.addAttribute("workpermit", workpermit);
        model.addAttribute("workpermitItems", workpermitItems);
        return "workPermitDetail";
    }
}
