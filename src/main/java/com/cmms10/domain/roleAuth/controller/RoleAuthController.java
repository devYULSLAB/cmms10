package com.cmms10.domain.roleAuth.controller;

import com.cmms10.domain.roleAuth.entity.RoleAuth;
import com.cmms10.domain.roleAuth.entity.RoleAuthIdClass;
import com.cmms10.domain.roleAuth.service.RoleAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * cmms10 - RoleAuthController
 * 역할 권한 관리 컨트롤러
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Controller
@RequestMapping("/roleAuth/{roleId}")
public class RoleAuthController {

    private final RoleAuthService roleService;

    public RoleAuthController(RoleAuthService roleService) {
        this.roleService = roleService;
    }

    /** 역할별 권한 목록 조회 */
    @GetMapping("/roleAuthList")
    public String listRoleAuth(@PathVariable String roleId, Model model) {
        model.addAttribute("roleId", roleId);
        List<RoleAuth> roleAuths = roleService.getRoleAuthByRoleId(roleId);
        model.addAttribute("roleAuths", roleAuths);
        return "domain/roleAuth/roleAuthList";
    }

    /** 역할 권한 등록/수정 화면 */
    @GetMapping("/roleAuthForm")
    public String showRoleAuthForm(@PathVariable String roleId,
                                   @RequestParam(required = false) String authGranted,
                                   Model model) {
        RoleAuth roleAuth = (roleId != null && authGranted != null)
                ? roleService.getRoleAuthByRoleIdAndAuthGranted(roleId, authGranted).orElse(new RoleAuth())
                : new RoleAuth();
        roleAuth.setRoleId(roleId);
        model.addAttribute("roleAuth", roleAuth);
        List<String> authTypes = List.of("ROLE_ADMIN", "ROLE_USER");
        model.addAttribute("authTypes", authTypes);
        return "domain/roleAuth/roleAuthForm";
    }

    /** 역할 권한 저장 */
    @PostMapping("/roleAuthSave")
    public String saveRoleAuth(@PathVariable String roleId,
                               @ModelAttribute RoleAuth roleAuth,
                               RedirectAttributes redirectAttributes) {
        roleAuth.setRoleId(roleId);

        if (roleAuth.getAuthGranted() == null || roleAuth.getAuthGranted().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한 값은 필수 입력값입니다.");
            return "redirect:/roleAuth/" + roleId + "/roleAuthForm";
        }

        try {
            roleService.saveRoleAuth(roleAuth);
            redirectAttributes.addFlashAttribute("successMessage", "권한이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/roleAuth/" + roleId + "/roleAuthList";
    }

    /** 역할 권한 삭제 */
    @PostMapping("/roleAuthDelete")
    public String deleteRoleAuth(@PathVariable String roleId,
                                 @RequestParam String authGranted,
                                 RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRoleAuth(roleId, authGranted);
            redirectAttributes.addFlashAttribute("successMessage", "권한이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/roleAuth/" + roleId + "/roleAuthList";
    }
}
