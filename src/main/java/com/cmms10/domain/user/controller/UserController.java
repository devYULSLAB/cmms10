package com.cmms10.domain.user.controller;

import com.cmms10.domain.user.entity.User;
import com.cmms10.domain.dept.entity.Dept;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final DeptService deptService; // 부서 목록을 위한 서비스

    public UserController(UserService userService, DeptService deptService) {
        this.userService = userService;
        this.deptService = deptService;
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
        // 세션에서 companyId를 가져와서 사이트와 부서 목록을 조회
        List <Dept> deptList = deptService.getAllDeptsByCompanyId(companyId);
        model.addAttribute("deptList", deptList);
        // 신규 사용자 등록을 위한 User 객체 생성
        User user = new User();
        model.addAttribute("user", user);
        return "domain/user/userForm";
    }

    @GetMapping("/userForm/{companyId}/{username}")
    public String editForm(@PathVariable String companyId,
                       @PathVariable String username,
                       Model model, HttpSession session) {
        // 세션에서 companyId를 가져와서 부서 목록을 조회
        List <Dept> deptList = deptService.getAllDeptsByCompanyId(companyId);
        model.addAttribute("deptList", deptList);

        User user = userService.getUserByCompanyIdAndUsername(companyId, username);
        model.addAttribute("user", user);
        return "domain/user/userForm";
    }

    @PostMapping("/userSave")
    public String save(@ModelAttribute User user, Model model, HttpSession session, @RequestParam String mode) {
        String username = (String) session.getAttribute("username");
        userService.saveUser(user, username, mode);
        return "redirect:/user/userList";
    }

    @PostMapping("/userDelete/{companyId}/{username}")
    public String delete(@PathVariable String companyId, @PathVariable String username, HttpSession session) {
        String usernameFromSession = (String) session.getAttribute("username");
        userService.deleteUser(companyId, username, usernameFromSession);
        return "redirect:/user/userList";
    }
}
