package com.cmms10.storMaster.controller;

import com.cmms10.storMaster.entity.StorMaster;
import com.cmms10.storMaster.entity.StorMasterId;
import com.cmms10.storMaster.service.StorMasterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/storMaster")
public class StorMasterController {
    private final StorMasterService storMasterService;

    public StorMasterController(StorMasterService storMasterService) {
        this.storMasterService = storMasterService;
    }

    @GetMapping("/storMasterList")
    public String list(Model model) {
        List<StorMaster> storList = storMasterService.findAll();
        model.addAttribute("storList", storList);
        return "storMaster/list";
    }

    @GetMapping("/storMasterForm")
    public String form(Model model) {
        model.addAttribute("storMaster", new StorMaster());
        return "storMaster/form";
    }

    @GetMapping("/storMasterForm/{companyId}/{siteId}/{locId}")
    public String editForm(@PathVariable String companyId, @PathVariable String siteId, @PathVariable String locId, Model model) {
        Optional<StorMaster> storMaster = storMasterService.findById(new StorMasterId(companyId, siteId, locId));
        model.addAttribute("storMaster", storMaster.orElse(new StorMaster()));
        return "storMaster/editForm";
    }

    @PostMapping("/storMasterSave")
    public String save(@ModelAttribute StorMaster storMaster) {
        storMasterService.save(storMaster);
        return "redirect:/storMaster/list";
    }

    @PostMapping("/storMasterDelete")
    public String delete(@ModelAttribute StorMasterId id) {
        storMasterService.deleteById(id);
        return "redirect:/storMaster/list";
    }
} 