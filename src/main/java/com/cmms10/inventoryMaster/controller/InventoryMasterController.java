package com.cmms10.inventoryMaster.controller;

import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.storMaster.entity.StorMaster;
import com.cmms10.domain.site.entity.Site;
import com.cmms10.inventoryMaster.dto.InventoryTransactionForm;
import com.cmms10.inventoryMaster.service.InventoryMasterService;
import com.cmms10.inventoryMaster.service.InventoryTransactionService;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.commonCode.service.CommonCodeService;
import com.cmms10.storMaster.service.StorMasterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * InventoryMasterController
 * - 재고 마스터/거래 화면 및 저장 처리
 * - 톱레벨 List 바인딩 NPE 해결: 래퍼 DTO(InventoryTransactionForm) 사용
 */
@Controller
@RequestMapping("/inventoryMaster")
public class InventoryMasterController {

    private final InventoryMasterService inventoryMasterService;
    private final InventoryTransactionService inventoryTransactionService;
    private final DeptService deptService;
    private final SiteService siteService;
    private final CommonCodeService commonCodeService;
    private final StorMasterService storMasterService;

    public InventoryMasterController(InventoryMasterService inventoryMasterService,
            InventoryTransactionService inventoryTransactionService,
            DeptService deptService,
            SiteService siteService,
            CommonCodeService commonCodeService,
            StorMasterService storMasterService) {
        this.inventoryMasterService = inventoryMasterService;
        this.inventoryTransactionService = inventoryTransactionService;
        this.deptService = deptService;
        this.siteService = siteService;
        this.commonCodeService = commonCodeService;
        this.storMasterService = storMasterService;
    }

    // ========================= 재고 마스터 화면/CRUD =========================

    /** 새로운 재고 마스터 등록 폼 */
    @GetMapping("/inventoryMasterForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        InventoryMaster inventoryMaster = new InventoryMaster();
        inventoryMaster.setCompanyId(companyId);

        model.addAttribute("inventoryMaster", inventoryMaster);
        model.addAttribute("respDepts", deptService.getAllDeptsByCompanyId(companyId));
        model.addAttribute("assetTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "ASSET"));
        return "inventoryMaster/inventoryMasterForm";
    }

    /** 재고 마스터 수정 폼 */
    @GetMapping("/inventoryMasterForm/{inventoryId}")
    public String editForm(@PathVariable String inventoryId,
            Model model,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        InventoryMaster inventoryMaster = inventoryMasterService.getInventoryMasterByCompanyIdAndInventoryId(companyId,
                inventoryId);

        model.addAttribute("inventoryMaster", inventoryMaster);
        model.addAttribute("respDepts", deptService.getAllDeptsByCompanyId(companyId));
        model.addAttribute("assetTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "ASSET"));

        return "inventoryMaster/inventoryMasterForm";
    }

    /** 재고 마스터 저장 */
    @PostMapping("/inventoryMasterSave")
    public String save(@ModelAttribute InventoryMaster inventoryMaster,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        inventoryMaster.setCompanyId(companyId);
        inventoryMasterService.saveInventoryMaster(inventoryMaster, username);

        return "redirect:/inventoryMaster/inventoryMasterList";
    }

    /** 재고 마스터 목록 조회 */
    @GetMapping("/inventoryMasterList")
    public String list(Model model,
            HttpSession session,
            @PageableDefault(size = 10, sort = "inventoryId") Pageable pageable) {
        String companyId = (String) session.getAttribute("companyId");

        Page<InventoryMaster> inventories = inventoryMasterService.getAllInventoryMasters(companyId, pageable);
        model.addAttribute("inventories", inventories);

        return "inventoryMaster/inventoryMasterList";
    }

    /** 재고 마스터 상세 조회 */
    @GetMapping("/inventoryMasterDetail/{inventoryId}")
    public String detail(@PathVariable String inventoryId,
            Model model,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        InventoryMaster inventoryMaster = inventoryMasterService.getInventoryMasterByCompanyIdAndInventoryId(companyId,
                inventoryId);
        model.addAttribute("inventoryMaster", inventoryMaster);
        return "inventoryMaster/inventoryMasterDetail";
    }

    /** 재고 마스터 삭제 */
    @PostMapping("/inventoryMasterDelete/{inventoryId}")
    public String delete(@PathVariable String inventoryId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        try {
            inventoryMasterService.deleteInventoryMaster(companyId, inventoryId, username);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting inventory master: " + e.getMessage());
        }

        return "redirect:/inventoryMaster/inventoryMasterList";
    }

    // ========================= 재고 거래 화면 =========================

    /** 재고 거래 화면 */
    @GetMapping("/inventoryTransaction")
    public String inventoryTransaction(Model model, HttpSession session, HttpServletRequest request) {
        String companyId = (String) session.getAttribute("companyId");

        model.addAttribute("companyId", companyId);
        model.addAttribute("contextPath", request.getContextPath());

        // 사이트 목록
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));

        // 첫 사이트가 있으면 위치 목록도 제공(선택 사항)
        List<Site> sites = siteService.getAllSitesByCompanyId(companyId);
        if (!sites.isEmpty()) {
            String firstSiteId = sites.get(0).getSiteId();
            model.addAttribute("locations", getLocationsForSite(companyId, firstSiteId));
        }

        return "inventoryMaster/inventoryTransaction";
    }

    /** 특정 사이트의 저장위치 목록 조회 */
    private List<StorMaster> getLocationsForSite(String companyId, String siteId) {
        try {
            return storMasterService.findByCompanyIdAndSiteId(companyId, siteId);
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    // ========================= 공통 유틸(빈행/검증) =========================

    private List<InventoryHistory> filterValidRows(List<InventoryHistory> rows) {
        if (rows == null)
            return List.of();
        return rows.stream()
                .filter(Objects::nonNull)
                .filter(r -> isNotBlank(r.getInventoryId()))
                .filter(r -> r.getQty() != null && gtZero(r.getQty()))
                .filter(r -> r.getUnitPrice() != null && gteZero(r.getUnitPrice()))
                .collect(Collectors.toList());
    }

    private boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private boolean gtZero(BigDecimal v) {
        return v.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean gteZero(BigDecimal v) {
        return v.compareTo(BigDecimal.ZERO) >= 0;
    }

    // ========================= 저장(입고/출고/이동/조정): 래퍼 DTO 사용
    // =========================
    // 컨트롤러에서 companyId/siteId/txType/txDate를 세팅해 서비스로 전달합니다.

    /** 입고 저장: toLocation 필수 */
    @PostMapping("/inventoryInboundSave")
    public String inventoryInboundSave(@ModelAttribute InventoryTransactionForm form,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");
        String siteId = form.getSiteId();

        List<InventoryHistory> valid = filterValidRows(form.getInventoryHistoryList()).stream()
                .filter(r -> isNotBlank(r.getToLocation()))
                .peek(h -> {
                    h.setCompanyId(companyId);
                    h.setSiteId(siteId);
                    h.setTransactionType("INBOUND");
                    h.setTransactionDate(java.time.LocalDateTime.now());
                    // historyId 자동 생성
                })
                .collect(Collectors.toList());

        if (valid.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "입고 저장: 유효한 행이 없습니다. (재고ID/수량/단가/입고위치 확인)");
            return "redirect:/inventoryMaster/inventoryTransaction";
        }

        try {
            inventoryTransactionService.processInbound(valid, username);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("재고 입고 %d건이 성공적으로 저장되었습니다.", valid.size()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "재고 입고 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/inventoryMaster/inventoryTransaction";
    }

    /** 출고 저장: fromLocation 필수 */
    @PostMapping("/inventoryOutboundSave")
    public String inventoryOutboundSave(@ModelAttribute InventoryTransactionForm form,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");
        String siteId = form.getSiteId();

        List<InventoryHistory> valid = filterValidRows(form.getInventoryHistoryList()).stream()
                .filter(r -> isNotBlank(r.getFromLocation()))
                .peek(h -> {
                    h.setCompanyId(companyId);
                    h.setSiteId(siteId);
                    h.setTransactionType("OUTBOUND");
                    h.setTransactionDate(java.time.LocalDateTime.now());
                    // historyId 자동 생성
                })
                .collect(Collectors.toList());

        if (valid.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "출고 저장: 유효한 행이 없습니다. (재고ID/수량/단가/출고위치 확인)");
            return "redirect:/inventoryMaster/inventoryTransaction";
        }

        try {
            inventoryTransactionService.processOutbound(valid, username);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("재고 출고 %d건이 성공적으로 저장되었습니다.", valid.size()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "재고 출고 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/inventoryMaster/inventoryTransaction";
    }

    /** 이동 저장: fromLocation + toLocation 필수 */
    @PostMapping("/inventoryMovementSave")
    public String inventoryMovementSave(@ModelAttribute InventoryTransactionForm form,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");
        String siteId = form.getSiteId();

        List<InventoryHistory> valid = filterValidRows(form.getInventoryHistoryList()).stream()
                .filter(r -> isNotBlank(r.getFromLocation()))
                .filter(r -> isNotBlank(r.getToLocation()))
                .peek(h -> {
                    h.setCompanyId(companyId);
                    h.setSiteId(siteId);
                    h.setTransactionType("MOVEMENT");
                    h.setTransactionDate(java.time.LocalDateTime.now());
                    // historyId 자동 생성
                })
                .collect(Collectors.toList());

        if (valid.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "이동 저장: 유효한 행이 없습니다. (재고ID/수량/단가/출발·도착위치 확인)");
            return "redirect:/inventoryMaster/inventoryTransaction";
        }

        try {
            inventoryTransactionService.processMovement(valid, username);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("재고 이동 %d건이 성공적으로 저장되었습니다.", valid.size()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "재고 이동 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/inventoryMaster/inventoryTransaction";
    }

    /** 조정 저장: (여기서는 toLocation 기준) */
    @PostMapping("/inventoryAdjustmentSave")
    public String inventoryAdjustmentSave(@ModelAttribute InventoryTransactionForm form,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");
        String siteId = form.getSiteId();

        List<InventoryHistory> valid = filterValidRows(form.getInventoryHistoryList()).stream()
                .filter(r -> isNotBlank(r.getToLocation()))
                .peek(h -> {
                    h.setCompanyId(companyId);
                    h.setSiteId(siteId);
                    h.setTransactionType("ADJUSTMENT");
                    h.setTransactionDate(java.time.LocalDateTime.now());
                    // historyId 자동 생성
                })
                .collect(Collectors.toList());

        if (valid.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "조정 저장: 유효한 행이 없습니다. (재고ID/수량/단가/위치 확인)");
            return "redirect:/inventoryMaster/inventoryTransaction";
        }

        try {
            inventoryTransactionService.processAdjustment(valid, username);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("재고 조정 %d건이 성공적으로 저장되었습니다.", valid.size()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "재고 조정 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/inventoryMaster/inventoryTransaction";
    }

    // ========================= 보조 API =========================

    /** 재고 ID로 재고명 조회 API */
    @GetMapping("/api/nameById")
    @ResponseBody
    public InventoryMaster getInventoryNameById(@RequestParam String companyId, @RequestParam String inventoryId) {
        try {
            return inventoryMasterService.getInventoryMasterByCompanyIdAndInventoryId(companyId, inventoryId);
        } catch (Exception e) {
            return null;
        }
    }

    /** 재고 ID와 위치 ID로 단가 조회 */
    @GetMapping("/api/unitPrice")
    @ResponseBody
    public BigDecimal getUnitPrice(@RequestParam String companyId, @RequestParam String siteId,
            @RequestParam String locId, @RequestParam String inventoryId) {
        try {
            return inventoryTransactionService.getUnitPrice(companyId, siteId, locId, inventoryId);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
