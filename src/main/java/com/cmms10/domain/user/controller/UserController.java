package com.cmms10.domain.user.controller;

import com.cmms10.domain.user.entity.User;
import com.cmms10.domain.dept.entity.Dept;
import com.cmms10.domain.roleAuth.entity.RoleAuth;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.domain.user.service.UserService;
import com.cmms10.domain.roleAuth.service.RoleAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final DeptService deptService;
    private final RoleAuthService roleAuthService;

    public UserController(UserService userService, DeptService deptService, RoleAuthService roleAuthService) {
        this.userService = userService;
        this.deptService = deptService;
        this.roleAuthService = roleAuthService;
    }

    @GetMapping("/userList")
    public String list(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<User> users = userService.getAllUsersByCompanyId(companyId);
        model.addAttribute("users", users);
        return "domain/user/userList";
    }

    @GetMapping("/userForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        // 신규 사용자 등록을 위한 User 객체 생성
        User user = new User();
        user.setCompanyId(companyId);
        // 부서 목록을 조회
        List<Dept> deptList = deptService.getAllDeptsByCompanyId(companyId);
        model.addAttribute("deptList", deptList);
        // 권한 목록을 조회
        List<RoleAuth> roleAuthList = roleAuthService.getAllRoleAuths();
        model.addAttribute("roleAuthList", roleAuthList);

        model.addAttribute("user", user);
        model.addAttribute("mode", "new");
        return "domain/user/userForm";
    }

    @GetMapping("/userForm/{companyId}/{username}")
    public String editForm(@PathVariable String companyId,
            @PathVariable String username,
            Model model, HttpSession session) {
        // 부서 목로을 조회
        List<Dept> deptList = deptService.getAllDeptsByCompanyId(companyId);
        model.addAttribute("deptList", deptList);
        // 권한 목록을 조회
        List<RoleAuth> roleAuthList = roleAuthService.getAllRoleAuths();
        model.addAttribute("roleAuthList", roleAuthList);

        User user = userService.getUserByCompanyIdAndUsername(companyId, username);
        model.addAttribute("user", user);
        model.addAttribute("mode", "edit");
        return "domain/user/userForm";
    }

    @PostMapping("/userSave")
    public String save(@ModelAttribute User user, Model model, HttpSession session, @RequestParam String mode) {
        String username = (String) session.getAttribute("username");
        try {
            userService.saveUser(user, username, mode);
        } catch (RuntimeException e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", e.getMessage());
            return form(model, session);
        }
        return "redirect:/user/userList";
    }

    @PostMapping("/userDelete/{companyId}/{username}")
    public String delete(@PathVariable String companyId, @PathVariable String username, HttpSession session) {
        String usernameFromSession = (String) session.getAttribute("username");
        userService.deleteUser(companyId, username, usernameFromSession);
        return "redirect:/user/userList";
    }
}
