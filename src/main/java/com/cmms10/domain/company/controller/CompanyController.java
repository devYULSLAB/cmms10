package com.cmms10.domain.company.controller;

import com.cmms10.domain.company.entity.Company;
import com.cmms10.domain.company.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companyList")
    public String list(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "domain/company/companyList";
    }

    @GetMapping("/companyForm")
    public String form(@RequestParam(required = false) String companyId, Model model) {
        Company company = companyId != null ? companyService.getCompanyById(companyId).orElse(new Company()) : new Company();
        model.addAttribute("company", company);
        return "domain/company/companyForm";
    }

    @PostMapping("/companySave")
    public String save(@ModelAttribute Company company) {
        companyService.saveCompany(company);
        return "redirect:/company/companyList";
    }

    @PostMapping("/companyDelete/{id}")
    public String delete(@PathVariable String id) {
        companyService.deleteCompany(id);
        return "redirect:/company/companyList";
    }
}
