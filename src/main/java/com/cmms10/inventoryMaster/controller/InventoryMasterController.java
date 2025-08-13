package com.cmms10.inventoryMaster.controller;

import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.inventoryMaster.service.InventoryMasterService;
import com.cmms10.inventoryMaster.service.InventoryTransactionService;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.commonCode.service.CommonCodeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final InventoryTransactionService inventoryTransactionService;
    private final DeptService deptService;
    private final SiteService siteService;
    private final CommonCodeService commonCodeService;

    public InventoryMasterController(InventoryMasterService inventoryMasterService,
            InventoryTransactionService inventoryTransactionService,
            DeptService deptService,
            SiteService siteService,
            CommonCodeService commonCodeService) {
        this.inventoryMasterService = inventoryMasterService;
        this.inventoryTransactionService = inventoryTransactionService;
        this.deptService = deptService;
        this.siteService = siteService;
        this.commonCodeService = commonCodeService;
    }

    /**
     * 새로운 재고 마스터 등록 폼
     * 
     * @param model   모델
     * @param session 세션
     * @return 뷰 이름
     */
    @GetMapping("/inventoryMasterForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        InventoryMaster inventoryMaster = new InventoryMaster();
        inventoryMaster.setCompanyId(companyId);

        model.addAttribute("inventoryMaster", inventoryMaster);
        // Select box 데이터 추가
        model.addAttribute("respDepts", deptService.getAllDeptsByCompanyId(companyId));
        model.addAttribute("assetTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "ASSET"));
        return "inventoryMaster/inventoryMasterForm";
    }

    /**
     * 재고 마스터 수정 폼
     * 
     * @param inventoryId 재고 ID
     * @param model       모델
     * @param session     세션
     * @return 뷰 이름
     */
    @GetMapping("/inventoryMasterForm/{inventoryId}")
    public String form(@PathVariable String inventoryId,
            Model model,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        InventoryMaster inventoryMaster = inventoryMasterService.getInventoryMasterByCompanyIdAndInventoryId(companyId,
                inventoryId);

        model.addAttribute("inventoryMaster", inventoryMaster);

        // Select box 데이터 추가
        model.addAttribute("respDepts", deptService.getAllDeptsByCompanyId(companyId));
        model.addAttribute("assetTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "ASSET"));

        return "inventoryMaster/inventoryMasterForm";
    }

    /**
     * 재고 마스터 목록 조회
     * 
     * @param model    모델
     * @param session  세션
     * @param pageable 페이지 정보
     * @return 뷰 이름
     */
    @GetMapping("/inventoryMasterList")
    public String list(Model model,
            HttpSession session,
            @PageableDefault(size = 10, sort = "inventoryId") Pageable pageable) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        Page<InventoryMaster> inventories = inventoryMasterService.getAllInventoryMasters(companyId, pageable);
        model.addAttribute("inventories", inventories);

        return "inventoryMaster/inventoryMasterList";
    }

    /**
     * 재고 마스터 목록 조회 (페이지네이션용)
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID (사용하지 않지만 호환성을 위해 유지)
     * @param model     모델
     * @param session   세션
     * @param pageable  페이지 정보
     * @return 뷰 이름
     */
    @GetMapping("/inventoryList")
    public String listWithParams(@RequestParam(required = false) String companyId,
            @RequestParam(required = false) String siteId,
            Model model,
            HttpSession session,
            @PageableDefault(size = 10, sort = "inventoryId") Pageable pageable) {
        // 세션에서 사용자 정보 가져오기
        String sessionCompanyId = (String) session.getAttribute("companyId");

        // 파라미터로 전달된 companyId가 있으면 사용, 없으면 세션에서 가져온 값 사용
        String targetCompanyId = (companyId != null && !companyId.trim().isEmpty()) ? companyId : sessionCompanyId;

        Page<InventoryMaster> inventories = inventoryMasterService.getAllInventoryMasters(targetCompanyId, pageable);
        model.addAttribute("inventories", inventories);

        return "inventoryMaster/inventoryMasterList";
    }

    /**
     * 재고 마스터 저장
     * 
     * @param inventoryMaster    재고 마스터 정보
     * @param session            세션
     * @param redirectAttributes 리다이렉트 속성
     * @return 리다이렉트 URL
     */
    @PostMapping("/inventoryMasterSave")
    public String save(@ModelAttribute InventoryMaster inventoryMaster,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        inventoryMaster.setCompanyId(companyId);

        inventoryMasterService.saveInventoryMaster(inventoryMaster, username);

        return "redirect:/inventoryMaster/inventoryMasterList";
    }

    /**
     * 재고 마스터 상세 조회
     * 
     * @param inventoryId 재고 ID
     * @param model       모델
     * @param session     세션
     * @return 뷰 이름
     */
    @GetMapping("/inventoryMasterDetail/{inventoryId}")
    public String detail(@PathVariable String inventoryId,
            Model model,
            HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        InventoryMaster inventoryMaster = inventoryMasterService.getInventoryMasterByCompanyIdAndInventoryId(companyId,
                inventoryId);
        model.addAttribute("inventoryMaster", inventoryMaster);
        return "inventoryMaster/inventoryMasterDetail";
    }

    /**
     * 재고 마스터 삭제
     * 
     * @param inventoryId        재고 ID
     * @param session            세션
     * @param redirectAttributes 리다이렉트 속성
     * @return 리다이렉트 URL
     */
    @PostMapping("/inventoryMasterDelete/{inventoryId}")
    public String delete(@PathVariable String inventoryId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        try {
            inventoryMasterService.deleteInventoryMaster(companyId, inventoryId, username);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting inventory master: " + e.getMessage());
        }

        return "redirect:/inventoryMaster/inventoryMasterList";
    }

    /**
     * 재고 거래 화면
     * 
     * @param model   모델
     * @param session 세션
     * @return 뷰 이름
     */
    @GetMapping("/inventoryTransaction")
    public String inventoryTransaction(Model model, HttpSession session, HttpServletRequest request) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        model.addAttribute("companyId", companyId);
        model.addAttribute("contextPath", request.getContextPath());

        // 사이트 목록 조회 (select box용)
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));

        return "inventoryMaster/inventoryTransaction";
    }

    /**
     * 재고 거래 저장
     * 
     * @param inventoryHistoryList 재고 거래 정보 리스트
     * @param session              세션
     * @param redirectAttributes   리다이렉트 속성
     * @return 리다이렉트 URL
     */
    @PostMapping("/inventoryTransactionSave")
    public String inventoryTransactionSave(
            @ModelAttribute("inventoryHistoryList") List<InventoryHistory> inventoryHistoryList,
            @RequestParam("siteId") String siteId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            // 세션에서 사용자 정보 가져오기
            String companyId = (String) session.getAttribute("companyId");
            String username = (String) session.getAttribute("username");

            // 빈 행 제거 및 siteId 설정
            List<InventoryHistory> validTransactions = inventoryHistoryList.stream()
                    .filter(history -> history.getInventoryId() != null && !history.getInventoryId().trim().isEmpty())
                    .peek(history -> {
                        history.setCompanyId(companyId);
                        history.setSiteId(siteId);
                    })
                    .collect(Collectors.toList());

            if (!validTransactions.isEmpty()) {
                inventoryTransactionService.processInventoryIo(validTransactions, username);
                redirectAttributes.addFlashAttribute("successMessage", "재고 거래가 성공적으로 저장되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "저장할 데이터가 없습니다.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "재고 거래 저장 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/inventoryMaster/inventoryTransaction";
    }

    /**
     * 재고 ID로 재고명 조회 API
     * 
     * @param companyId   회사 ID
     * @param inventoryId 재고 ID
     * @return 재고명 정보
     */
    @GetMapping("/api/nameById")
    @ResponseBody
    public InventoryMaster getInventoryNameById(@RequestParam String companyId, @RequestParam String inventoryId) {
        try {
            return inventoryMasterService.getInventoryMasterByCompanyIdAndInventoryId(companyId, inventoryId);
        } catch (Exception e) {
            return null;
        }
    }
}
