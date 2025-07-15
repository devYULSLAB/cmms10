/**
 * 패키지: com.cmms10.domain.roleAuth.controller
 * 프로그램명: RoleAuthController.java
 * 주요기능: 역할별 권한 관리(목록, 등록, 수정, 삭제, 저장)
 * 생성자: cmms10
 * 생성일: 2024-07-13
 */
package com.cmms10.domain.roleAuth.controller;

import com.cmms10.domain.roleAuth.entity.RoleAuth;
import com.cmms10.domain.roleAuth.service.RoleAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/roleAuth")
public class RoleAuthController {

    private final RoleAuthService roleAuthService;

    public RoleAuthController(RoleAuthService roleAuthService) {
        this.roleAuthService = roleAuthService;
    }

    /**
     * 역할별 권한 목록 조회
     * @param roleId 역할ID
     */
    @GetMapping("/roleAuthList")
    public String list(Model model) {
        List<RoleAuth> roleAuths = roleAuthService.getAllRoleAuths();
        model.addAttribute("roleAuths", roleAuths);
        return "domain/roleAuth/roleAuthList";
    }

    /**
     * 권한 등록 폼
     * @param roleId 역할ID
     */
    @GetMapping("/roleAuthForm")
    public String form(Model model) {
        RoleAuth roleAuth = new RoleAuth();
        model.addAttribute("roleAuth", roleAuth);
        model.addAttribute("mode", "new");
        return "domain/roleAuth/roleAuthForm";
    }

    /**
     * 권한 수정 폼
     * @param roleId 역할ID
     * @param authGranted 권한명
     */
    @GetMapping("/roleAuthForm/{roleId}/{authGranted}")
    public String editform(@PathVariable String roleId, @PathVariable String authGranted, Model model) {
        RoleAuth roleAuth = roleAuthService.getRoleAuthByRoleIdAndAuthGranted(roleId, authGranted);
        model.addAttribute("roleAuth", roleAuth);
        model.addAttribute("mode", "edit");
        return "domain/roleAuth/roleAuthForm";
    }

    /**
     * 권한 저장(신규/수정)
     * @param roleAuth 권한 엔티티
     */
    @PostMapping("/roleAuthSave")
    public String save(@ModelAttribute RoleAuth roleAuth, @RequestParam(required = false) String mode, Model model) {
        try {
            roleAuthService.saveRoleAuth(roleAuth);
            return "redirect:/roleAuth/roleAuthList";
        } catch (Exception e) {
            model.addAttribute("roleAuth", roleAuth);
            model.addAttribute("errorMessage", "저장 중 오류가 발생했습니다: " + e.getMessage());
            model.addAttribute("mode", mode != null ? mode : "new");
            return "domain/roleAuth/roleAuthForm";
        }
    }

    /**
     * 권한 삭제
     * @param roleId 역할ID
     * @param authGranted 권한명
     */
    @PostMapping("/roleAuthDelete/{roleId}/{authGranted}")
    public String delete(@PathVariable String roleId, @PathVariable String authGranted, RedirectAttributes redirectAttributes) {
        try {
            roleAuthService.deleteRoleAuth(roleId, authGranted);
            redirectAttributes.addFlashAttribute("successMessage", "권한이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/roleAuth/roleAuthList";
    }
}
