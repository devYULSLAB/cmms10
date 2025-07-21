package com.cmms10.domain.company.controller;

import com.cmms10.domain.company.entity.Company;
import com.cmms10.domain.company.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companyForm")
    public String form(Model model) {
        Company company = new Company();
        model.addAttribute("company", company);
        model.addAttribute("mode", "new");
        return "domain/company/companyForm";
    }

    @GetMapping("/companyForm/{companyId}")
    public String editForm(@PathVariable String companyId, Model model) {
        Company company = companyService.getCompanyById(companyId);
        model.addAttribute("company", company);
        model.addAttribute("mode", "edit");
        return "domain/company/companyForm";
    }
    
    @GetMapping("/companyList")
    public String list(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "domain/company/companyList";
    }

    @PostMapping("/companySave")
    public String save(@ModelAttribute Company company, HttpSession session, Model model, @RequestParam String mode) {
        String username = (String) session.getAttribute("username");
        try {
            companyService.saveCompany(company, username, mode);
        } catch (RuntimeException e) {
            // 예외 처리: 중복된 회사 ID 등
            model.addAttribute("company", company);
            model.addAttribute("errorMessage", e.getMessage());
            return "domain/company/companyForm"; // 폼을 바로 반환
        }
        return "redirect:/company/companyList";
    }

    @PostMapping("/companyDelete/{id}")
    public String delete(@PathVariable String id, HttpSession session) {
        String username = (String) session.getAttribute("username");
        companyService.deleteCompany(id, username);
        return "redirect:/company/companyList";
    }
}
