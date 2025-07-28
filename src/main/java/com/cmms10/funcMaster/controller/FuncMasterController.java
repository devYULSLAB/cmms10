package com.cmms10.funcMaster.controller;

import com.cmms10.funcMaster.entity.FuncMaster;
import com.cmms10.funcMaster.service.FuncMasterService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.domain.site.entity.Site;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * 프로그램명: CMMS10
 * 패키지명: com.cmms10.funcMaster.controller
 * 클래스명: FuncMasterController
 * 주요기능: FuncMaster 엔티티의 웹 요청 처리
 * 생성자: AI Assistant
 * 생성일: 2024-12-19
 */
@Controller
@RequestMapping("/funcMaster")
public class FuncMasterController {

    private final FuncMasterService funcMasterService;
    private final SiteService siteService;

    public FuncMasterController(FuncMasterService funcMasterService, SiteService siteService) {
        this.funcMasterService = funcMasterService;
        this.siteService = siteService;
    }

    /**
     * 기능 마스터 목록
     * 
     * @param model   모델
     * @param session 세션
     * @return 뷰 이름
     */
    @GetMapping("/funcMasterList")
    public String funcMasterList(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<FuncMaster> funcMasters = funcMasterService.getAllFuncMastersByCompanyId(companyId);
        model.addAttribute("funcMasters", funcMasters);
        model.addAttribute("companyId", companyId);
        return "funcMaster/funcMasterList";
    }

    /**
     * 기능 마스터 등록 폼
     * 
     * @param model   모델
     * @param session 세션
     * @return 뷰 이름
     */
    @GetMapping("/funcMasterForm")
    public String funcMasterForm(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        // 사이트 목록 조회
        List<Site> siteList = siteService.getAllSitesByCompanyId(companyId);

        FuncMaster funcMaster = new FuncMaster();
        funcMaster.setCompanyId(companyId);

        model.addAttribute("funcMaster", funcMaster);
        model.addAttribute("siteList", siteList);
        model.addAttribute("mode", "new");
        return "funcMaster/funcMasterForm";
    }

    /**
     * 기능 마스터 수정 폼
     * 
     * @param funcId    기능 ID
     * @param companyId 회사 ID
     * @param model     모델
     * @return 뷰 이름
     */
    @GetMapping("/funcMasterForm/{funcId}/{companyId}")
    public String funcMasterForm(@PathVariable String funcId, @PathVariable String companyId, Model model) {
        // 사이트 목록 조회
        List<Site> siteList = siteService.getAllSitesByCompanyId(companyId);

        FuncMaster funcMaster = funcMasterService.getFuncMasterByCompanyIdAndSiteIdAndFuncId(companyId, null, funcId);

        model.addAttribute("funcMaster", funcMaster);
        model.addAttribute("siteList", siteList);
        model.addAttribute("mode", "edit");
        return "funcMaster/funcMasterForm";
    }

    /**
     * 기능 마스터 저장
     * 
     * @param funcMaster         기능 마스터
     * @param mode               모드
     * @param redirectAttributes 리다이렉트 속성
     * @return 리다이렉트 URL
     */
    @PostMapping("/funcMasterSave")
    public String funcMasterSave(@ModelAttribute FuncMaster funcMaster,
            @RequestParam(required = false) String mode,
            RedirectAttributes redirectAttributes) {
        try {
            funcMasterService.saveFuncMaster(funcMaster);
            redirectAttributes.addFlashAttribute("successMessage", "기능 마스터가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "기능 마스터 저장에 실패했습니다: " + e.getMessage());
        }
        return "redirect:/funcMaster/funcMasterList";
    }

    /**
     * 기능 마스터 삭제
     * 
     * @param funcId             기능 ID
     * @param companyId          회사 ID
     * @param redirectAttributes 리다이렉트 속성
     * @return 리다이렉트 URL
     */
    @PostMapping("/funcMasterDelete/{funcId}/{companyId}")
    public String funcMasterDelete(@PathVariable String funcId,
            @PathVariable String companyId,
            RedirectAttributes redirectAttributes) {
        try {
            funcMasterService.deleteFuncMaster(companyId, null, funcId);
            redirectAttributes.addFlashAttribute("successMessage", "기능 마스터가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "기능 마스터 삭제에 실패했습니다: " + e.getMessage());
        }
        return "redirect:/funcMaster/funcMasterList";
    }
}