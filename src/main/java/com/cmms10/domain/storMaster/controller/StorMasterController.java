package com.cmms10.domain.storMaster.controller;

import com.cmms10.domain.storMaster.entity.StorMaster;
import com.cmms10.domain.storMaster.entity.StorMasterIdClass;
import com.cmms10.domain.storMaster.service.StorMasterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/storMaster")
public class StorMasterController {
    private final StorMasterService storMasterService;

    public StorMasterController(StorMasterService storMasterService) {
        this.storMasterService = storMasterService;
    }

    @GetMapping("/storMasterList")
    public String list(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<StorMaster> storList = storMasterService.findByCompanyId(companyId);
        model.addAttribute("storList", storList);
        return "storMaster/list";
    }

    @GetMapping("/storMasterForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        StorMaster storMaster = new StorMaster();
        storMaster.setCompanyId(companyId);
        model.addAttribute("storMaster", storMaster);
        model.addAttribute("mode", "new");
        return "storMaster/form";
    }

    @GetMapping("/storMasterForm/{companyId}/{siteId}/{locId}")
    public String editForm(@PathVariable String companyId, @PathVariable String siteId, @PathVariable String locId, Model model) {
        StorMaster storMaster = storMasterService.findByCompanyIdAndSiteIdAndLocId(companyId, siteId, locId);
        model.addAttribute("storMaster", storMaster);
        model.addAttribute("mode", "edit");
        return "storMaster/editForm";
    }

    @PostMapping("/storMasterSave")
    public String save(@ModelAttribute StorMaster storMaster) {
        storMasterService.save(storMaster);
        return "redirect:/storMaster/list";
    }

    @PostMapping("/storMasterDelete")
    public String delete(@ModelAttribute StorMasterIdClass id) {
        storMasterService.deleteById(id);
        return "redirect:/storMaster/list";
    }
} 