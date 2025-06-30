package com.cmms10.domain.user.controller;

import com.cmms10.domain.user.entity.User;
import com.cmms10.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userList")
    public String list(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "domain/user/userList";
    }

    @GetMapping("/userForm")
    public String form(@RequestParam(required = false) String companyId,
                       @RequestParam(required = false) String username, 
                       Model model) {
        User user = (companyId != null && username != null) ?
                userService.getUser(companyId, username).orElse(new User()) : new User();
        model.addAttribute("user", user);
        return "domain/user/userForm";
    }

    @PostMapping("/userSave")
    public String save(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/user/userList";
    }

    @PostMapping("/userDelete/{companyId}/{username}")
    public String delete(@PathVariable String companyId, @PathVariable String username) {
        userService.deleteUser(companyId, username);
        return "redirect:/user/list";
    }
}
