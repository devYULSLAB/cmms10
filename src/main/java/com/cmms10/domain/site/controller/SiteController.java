package com.cmms10.domain.site.controller;

import com.cmms10.domain.site.entity.Site;
import com.cmms10.domain.site.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String list(Model model, Authentication auth) {
        model.addAttribute("siteList", siteService.findByCompanyId(auth.getName()));
        return "domain/site/siteList";
    }

    /**
     * 사이트 상세 화면
     */
    @GetMapping("/siteDetail/{companyId}/{siteId}")
    public String detail(@PathVariable String companyId, 
                        @PathVariable String siteId, 
                        Model model) {
        model.addAttribute("site", siteService.findById(companyId, siteId));
        return "domain/site/siteDetail";
    }

    /**
     * 사이트 등록/수정 화면
     */
    @GetMapping("/siteForm")
    public String form(@RequestParam(required = false) String companyId,
                      @RequestParam(required = false) String siteId,
                      Model model) {
        Site site = (companyId != null && siteId != null) ?
                siteService.findById(companyId, siteId) : new Site();
        model.addAttribute("site", site);
        return "domain/site/siteForm";
    }

    /**
     * 사이트 저장
     */
    @PostMapping("/siteSave")
    public String save(@ModelAttribute Site site) {
        siteService.save(site);
        return "redirect:/site/siteList";
    }

    /**
     * 사이트 삭제
     */
    @PostMapping("/siteDelete/{companyId}/{siteId}")
    public String delete(@PathVariable String companyId,
                        @PathVariable String siteId) {
        siteService.delete(companyId, siteId);
        return "redirect:/site/siteList";
    }
}