package com.cmms10.commonCode.controller;

import com.cmms10.commonCode.entity.CommonCode;
import com.cmms10.commonCode.entity.CommonCodeItem;
import com.cmms10.commonCode.service.CommonCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/commonCode")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    public CommonCodeController(CommonCodeService commonCodeService) {
        this.commonCodeService = commonCodeService;
    }

    /** 공통코드 목록 */
    @GetMapping("/commonCodeList")
    public String commonCodeList(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<CommonCode> codes = commonCodeService.getAllCommonCodes(companyId);
        model.addAttribute("codes", codes);
        return "commonCode/commonCodeList";
    }

    /** 공통코드 등록/수정 폼 */
    @GetMapping("/commonCodeForm")
    public String commonCodeForm(@RequestParam(required = false) String codeId,
                                 Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        CommonCode code = (codeId != null)
                ? commonCodeService.getCommonCode(companyId, codeId).orElse(new CommonCode())
                : new CommonCode();
        model.addAttribute("commonCode", code);
        return "commonCode/commonCodeForm";
    }

    /** 공통코드 저장 */
    @PostMapping("/commonCodeSave")
    public String commonCodeSave(@ModelAttribute CommonCode commonCode,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            commonCodeService.saveCommonCode(commonCode);
            redirectAttributes.addFlashAttribute("successMessage", "Common code saved successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save common code: " + e.getMessage());
        }
        return "redirect:/commonCode/commonCodeList";
    }

    /** 공통코드 삭제 */
    @PostMapping("/commonCodeDelete")
    public String commonCodeDelete(@ModelAttribute CommonCode commonCode,
                                   RedirectAttributes redirectAttributes) {
        try {
            commonCodeService.deleteCommonCode(commonCode);
            redirectAttributes.addFlashAttribute("successMessage", "Common code deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete common code: " + e.getMessage());
        }
        return "redirect:/commonCode/commonCodeList";
    }

    /** 공통코드 아이템 목록 */
    @GetMapping("/commonCodeItemList")
    public String commonCodeItemList(@RequestParam String codeId,
                                     @RequestParam String companyId,
                                     Model model) {
        CommonCode commonCode = commonCodeService.getCommonCode(companyId, codeId).orElse(null);
        List<CommonCodeItem> items = commonCodeService.getCommonCodeItems(companyId, codeId);
        model.addAttribute("commonCode", commonCode);
        model.addAttribute("items", items);
        model.addAttribute("companyId", companyId);
        return "commonCode/commonCodeItemList";
    }

    /** 공통코드 아이템 등록/수정 폼 */
    @GetMapping("/commonCodeItemForm")
    public String commonCodeItemForm(@RequestParam String codeId,
                                     @RequestParam String companyId,
                                     @RequestParam(required = false) String codeItemId,
                                     Model model) {
        CommonCode commonCode = commonCodeService.getCommonCode(companyId, codeId).orElse(null);
        CommonCodeItem item = (codeItemId != null)
                ? commonCodeService.getCommonCodeItem(companyId, codeId, codeItemId).orElse(new CommonCodeItem())
                : new CommonCodeItem();
        item.setCodeId(codeId);
        item.setCompanyId(companyId);
        model.addAttribute("commonCode", commonCode);
        model.addAttribute("commonCodeItem", item);
        return "commonCode/commonCodeItemForm";
    }

    /** 공통코드 아이템 저장 */
    @PostMapping("/commonCodeItemSave")
    public String commonCodeItemSave(@ModelAttribute CommonCodeItem commonCodeItem,
                                     RedirectAttributes redirectAttributes) {
        try {
            commonCodeService.saveCommonCodeItem(commonCodeItem);
            redirectAttributes.addFlashAttribute("successMessage", "Item saved successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save item: " + e.getMessage());
        }
        return "redirect:/commonCode/commonCodeItemList?codeId=" + commonCodeItem.getCodeId() + "&companyId=" + commonCodeItem.getCompanyId();
    }

    /** 공통코드 아이템 삭제 */
    @PostMapping("/commonCodeItemDelete")
    public String commonCodeItemDelete(@RequestParam String codeId,
                                       @RequestParam String companyId,
                                       @RequestParam String codeItemId,
                                       RedirectAttributes redirectAttributes) {
        try {
            CommonCodeItem item = commonCodeService.getCommonCodeItem(companyId, codeId, codeItemId).orElse(null);
            if (item != null) {
                commonCodeService.deleteCommonCodeItem(item);
                redirectAttributes.addFlashAttribute("successMessage", "Item deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Item not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete item: " + e.getMessage());
        }
        return "redirect:/commonCode/commonCodeItemList?codeId=" + codeId + "&companyId=" + companyId;
    }
}