package com.cmms10.commonCode.controller;

import com.cmms10.commonCode.entity.CommonCode;
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
        List<CommonCode> commonCodes = commonCodeService.getAllCommonCodesByCompanyId(companyId);
        model.addAttribute("commonCodes", commonCodes);
        model.addAttribute("companyId", companyId);
        return "commonCode/commonCodeList";
    }

    /** 공통코드 등록 폼 */
    @GetMapping("/commonCodeForm")
    public String commonCodeForm(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        CommonCode commonCode = new CommonCode();
        commonCode.setCompanyId(companyId);
        model.addAttribute("commonCode", commonCode);
        model.addAttribute("mode", "new");
        return "commonCode/commonCodeForm";
    }

    /** 공통코드 수정 폼 */
    @GetMapping("/commonCodeForm/{codeId}/{companyId}")
    public String commonCodeForm(@PathVariable String codeId, @PathVariable String companyId, Model model) {
        CommonCode commonCode = commonCodeService.getCommonCodeByCompanyIdAndCodeId(companyId, codeId);
        model.addAttribute("commonCode", commonCode);
        model.addAttribute("mode", "edit");
        return "commonCode/commonCodeForm";
    }

    /** 공통코드 저장 */
    @PostMapping("/commonCodeSave")
    public String commonCodeSave(@ModelAttribute CommonCode commonCode,
                                 @RequestParam(required = false) String mode,
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
    public String commonCodeDelete(@PathVariable String codeId,
                                   @PathVariable String companyId) {
        commonCodeService.deleteCommonCode(companyId, codeId);
        return "redirect:/commonCode/commonCodeList";
    }
}