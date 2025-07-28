package com.cmms10.storMaster.controller;

import com.cmms10.storMaster.entity.StorMaster;
import com.cmms10.storMaster.entity.StorMasterIdClass;
import com.cmms10.storMaster.service.StorMasterService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.domain.site.entity.Site;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/storMaster")
public class StorMasterController {
    private final StorMasterService storMasterService;
    private final SiteService siteService;

    public StorMasterController(StorMasterService storMasterService, SiteService siteService) {
        this.storMasterService = storMasterService;
        this.siteService = siteService;
    }

    @GetMapping("/storMasterForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        // 사이트 목록 조회
        List<Site> siteList = siteService.getAllSitesByCompanyId(companyId);

        StorMaster storMaster = new StorMaster();
        storMaster.setCompanyId(companyId);

        model.addAttribute("storMaster", storMaster);
        model.addAttribute("siteList", siteList);
        model.addAttribute("mode", "new");
        return "storMaster/storMasterForm";
    }

    @GetMapping("/storMasterForm/{companyId}/{siteId}/{locId}")
    public String editForm(@PathVariable String companyId, @PathVariable String siteId, @PathVariable String locId,
            Model model) {
        // 사이트 목록 조회
        List<Site> siteList = siteService.getAllSitesByCompanyId(companyId);

        StorMaster storMaster = storMasterService.findByCompanyIdAndSiteIdAndLocId(companyId, siteId, locId);

        model.addAttribute("storMaster", storMaster);
        model.addAttribute("siteList", siteList);
        model.addAttribute("mode", "edit");
        return "storMaster/storMasterForm";
    }

    @GetMapping("/storMasterList")
    public String list(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<StorMaster> storList = storMasterService.findByCompanyId(companyId);
        model.addAttribute("storList", storList);
        return "storMaster/storMasterList";
    }

    @PostMapping("/storMasterSave")
    public String save(@ModelAttribute StorMaster storMaster) {
        storMasterService.saveStorMaster(storMaster);
        return "redirect:/storMaster/storMasterList";
    }

    @PostMapping("/storMasterDelete")
    public String delete(@ModelAttribute StorMasterIdClass id) {
        storMasterService.deleteStorMaster(id.getCompanyId(), id.getSiteId(), id.getLocId());
        return "redirect:/storMaster/storMasterList";
    }
}