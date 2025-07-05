package com.cmms10.inventoryMaster.controller;

import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.service.InventoryMasterService;

import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

/**
 * cmms10 - InventoryMasterController
 * 재고 마스터 컨트롤러
 * 
 * @author cmms10
 * @since 2024-03-19
 */

@Controller
@RequestMapping("/inventoryMaster")
public class InventoryMasterController {

    private final InventoryMasterService inventoryMasterService;

    public InventoryMasterController(InventoryMasterService inventoryMasterService) {
        this.inventoryMasterService = inventoryMasterService;
    }

     /**
     * 재고 마스터 목록 조회
     * @param model 모델
     * @param session 세션
     * @param pageable 페이지 정보
     * @return 뷰 이름
     */
    @GetMapping("/inventoryMasterList")
    public String list(Model model, 
                            HttpSession session,
                            @PageableDefault(size = 10, sort = "inventoryId") Pageable pageable) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        String username = (String) session.getAttribute("username");
        
        Page<InventoryMaster> inventoryPage = inventoryMasterService.getAllInventoryMasters(companyId, siteId, pageable);
        model.addAttribute("inventoryPage", inventoryPage);
        model.addAttribute("companyId", companyId);
        model.addAttribute("siteId", siteId);
        
        return "inventoryMaster/inventoryMasterList";
    }

    /**
     * 새로운 재고 마스터 등록 폼
     * @param model 모델
     * @param session 세션
     * @return 뷰 이름
     */
    @GetMapping("/inventoryMasterForm")
    public String form(Model model, HttpSession session) {
        InventoryMaster inventoryMaster = new InventoryMaster();
        // 새로운 재고 마스터 폼을 위한 모델 초기화
        model.addAttribute("inventoryMaster", inventoryMaster);
        
        return "inventoryMaster/inventoryMasterForm";
    }

    /**
     * 재고 마스터 수정 폼
     * @param inventoryId 재고 ID
     * @param model 모델
     * @param session 세션
     * @return 뷰 이름
     */
    @GetMapping("/inventoryMasterForm/{inventoryId}")
    public String form(@PathVariable String inventoryId,
                       Model model,
                       HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        InventoryMaster inventoryMaster = inventoryMasterService.getInventoryMasterByInventoryId(companyId, inventoryId);
        model.addAttribute("inventoryMaster", inventoryMaster);

        return "inventoryMaster/inventoryMasterForm";
    }

    @GetMapping("/inventoryIoHistory")
    public String ioHistory(Model model, HttpSession session) {
        // String companyId = (String) session.getAttribute("companyId");
        // String siteId = (String) session.getAttribute("siteId");

        // model.addAttribute("companyId", companyId);
        // model.addAttribute("siteId", siteId);
        return "inventoryMaster/inventoryIoHistory";
    }

    /**
     * 재고 마스터 저장
     * @param inventoryMaster 재고 마스터 정보
     * @param session 세션
     * @param redirectAttributes 리다이렉트 속성
     * @return 리다이렉트 URL
     */
    @PostMapping("/inventoryMasterSave")
    public String save(@ModelAttribute InventoryMaster inventoryMaster,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        String username = (String) session.getAttribute("username");
        // 필수 정보 설정
        inventoryMaster.setCompanyId(companyId);
        inventoryMaster.setSiteId(siteId);

        inventoryMasterService.saveInventoryMaster(inventoryMaster, username);

        return "redirect:/inventoryMaster/inventoryMasterList";
    }

    /** 재고 마스터 상세 조회
     * @param inventoryId 재고 ID
     * @param model 모델
     * @param session 세션
     * @return 뷰 이름
     */
    @GetMapping("/inventoryMasterDetail/{inventoryId}")
    public String detail(@PathVariable String inventoryId,
                          Model model,
                          HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        InventoryMaster inventoryMaster = inventoryMasterService.getInventoryMasterByInventoryId(companyId, inventoryId);
        model.addAttribute("inventoryMaster", inventoryMaster);
        return "inventoryMaster/inventoryMasterDetail";
    }

    /**
     * 재고 마스터 삭제
     * @param inventoryId 재고 ID
     * @param session 세션
     * @param redirectAttributes 리다이렉트 속성
     * @return 리다이렉트 URL
     */
    @PostMapping("/inventoryMasterDelete/{inventoryId}")
    public String delete(@PathVariable String inventoryId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
                
        try {
            inventoryMasterService.deleteInventoryMaster(companyId, inventoryId);
            redirectAttributes.addFlashAttribute("successMessage", "Inventory Master '" + inventoryId + "' deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting Inventory Master: " + e.getMessage());
        }

        return "redirect:/inventoryMaster/inventoryMasterList";
    }
}
