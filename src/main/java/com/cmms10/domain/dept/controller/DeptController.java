package com.cmms10.domain.dept.controller;

import com.cmms10.domain.dept.entity.Dept;
import com.cmms10.domain.dept.service.DeptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dept")
public class DeptController {
    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping("/deptList")
    public String list(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<Dept> depts = deptService.getAllDeptsByCompanyId(companyId);
        model.addAttribute("depts", depts);
        return "domain/dept/deptList";
    }

    @GetMapping("/deptForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        Dept dept = new Dept();
        dept.setCompanyId(companyId);
        model.addAttribute("dept", dept);
        model.addAttribute("mode", "new");
        return "domain/dept/deptForm";
    }

    @GetMapping("/deptForm/{companyId}/{deptId}")
    public String editForm(@PathVariable String companyId,
                       @PathVariable String deptId,
                       Model model) {
        Dept dept = deptService.getDeptByCompanyIdAndDeptId(companyId, deptId);

        model.addAttribute("dept", dept);
        model.addAttribute("mode", "edit");
        return "domain/dept/deptForm";
    }

    @PostMapping("/deptSave")
    public String save(@ModelAttribute Dept dept, Model model,HttpSession session, @RequestParam String mode) {
        String username = (String) session.getAttribute("username");
        try {
            deptService.saveDept(dept, username, mode);
        } catch (RuntimeException e) {
            model.addAttribute("dept", dept);
            model.addAttribute("errorMessage", e.getMessage());
            return form(model, session);
        }
        return "redirect:/dept/deptList";
    }

    @PostMapping("/deptDelete/{companyId}/{deptId}")
    public String delete(@PathVariable String companyId, @PathVariable String deptId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        deptService.deleteDept(companyId, deptId, username);
        return "redirect:/dept/deptList";
    }
}
