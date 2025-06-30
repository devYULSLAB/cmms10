package com.cmms10.domain.dept.controller;

import com.cmms10.domain.dept.entity.Dept;
import com.cmms10.domain.dept.service.DeptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dept")
public class DeptController {
    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping("/deptList")
    public String list(Model model) {
        List<Dept> depts = deptService.getAllDepts();
        model.addAttribute("depts", depts);
        return "domain/dept/deptList";
    }

    @GetMapping("/deptForm")
    public String form(@RequestParam(required = false) String companyId,
                       @RequestParam(required = false) String deptId, 
                       Model model) {
        Dept dept = (companyId != null && deptId != null) ?
                deptService.getDeptById(companyId, deptId).orElse(new Dept()) : new Dept();
        model.addAttribute("dept", dept);
        return "domain/dept/deptForm";
    }

    @PostMapping("/deptSave")
    public String save(@ModelAttribute Dept dept) {
        deptService.saveDept(dept);
        return "redirect:/dept/deptList";
    }

    @PostMapping("/deptDelete/{companyId}/{deptId}")
    public String delete(@PathVariable String companyId, @PathVariable String deptId) {
        deptService.deleteDept(companyId, deptId);
        return "redirect:/dept/deptList";
    }
}
