package com.cmms10.domain.site.controller;

import com.cmms10.domain.site.entity.Site;
import com.cmms10.domain.site.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * cmms10 - SiteController
 * 사이트 관리 컨트롤러
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Controller
@RequestMapping("/site")
public class SiteController {

    @Autowired
    private SiteService siteService;

    /**
     * 사이트 목록 화면
     */
    @GetMapping("/siteList")
    public String list(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List <Site> siteList = siteService.getAllSitesByCompanyId(companyId);
        model.addAttribute("siteList", siteList);
        return "domain/site/siteList";
    }

    /**
     * 사이트 상세 화면
     */
    @GetMapping("/siteDetail/{companyId}/{siteId}")
    public String detail(@PathVariable String companyId, 
                        @PathVariable String siteId, 
                        Model model) {
        Site site = siteService.getSiteByCompanyIdAndSiteId(companyId, siteId);
        model.addAttribute("site", site);
        return "domain/site/siteDetail";
    }

    /**
     * 사이트 등록(신규) 폼
     */
    @GetMapping("/siteForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        Site site = new Site();
        site.setCompanyId(companyId);

        model.addAttribute("site", site);
        model.addAttribute("mode", "new");
        return "domain/site/siteForm";
    }

    /**
     * 사이트 수정 폼 (PathVariable 방식)
     */
    @GetMapping("/siteForm/{companyId}/{siteId}")
    public String editForm(@PathVariable String companyId,
                       @PathVariable String siteId,
                       Model model) {
        Site site = siteService.getSiteByCompanyIdAndSiteId(companyId, siteId);
        model.addAttribute("site", site);
        model.addAttribute("mode", "edit");
        return "domain/site/siteForm";
    }

    /**
     * 사이트 저장
     */
    @PostMapping("/siteSave")
    public String save(@ModelAttribute Site site, HttpSession session, Model model, @RequestParam String mode) {
        String username = (String) session.getAttribute("username");
        try {
            siteService.save(site, username, mode);
        } catch (RuntimeException e) {
            model.addAttribute("site", site);
            model.addAttribute("errorMessage", e.getMessage());
            return form(model, session);
        }
        return "redirect:/site/siteList";
    }

    /**
     * 사이트 삭제
     */
    @PostMapping("/siteDelete/{companyId}/{siteId}")
    public String delete(@PathVariable String companyId,
                        @PathVariable String siteId,
                        HttpSession session) {
        String username = (String) session.getAttribute("username");
        siteService.delete(companyId, siteId, username);
        return "redirect:/site/siteList";
    }
}