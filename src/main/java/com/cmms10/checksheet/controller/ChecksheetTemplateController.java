package com.cmms10.checksheet.controller;

import com.cmms10.checksheet.entity.ChecksheetTemplate;
import com.cmms10.checksheet.service.ChecksheetTemplateService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checksheet")
@RequiredArgsConstructor
public class ChecksheetTemplateController {

    private final ChecksheetTemplateService templateService;

    // 템플릿 작성 화면
    @GetMapping("/checksheetTemplateForm")
    public String templateForm(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        ChecksheetTemplate checksheetTemplate = new ChecksheetTemplate();
        checksheetTemplate.setCompanyId(companyId);
        model.addAttribute("checksheetTemplate", checksheetTemplate);
        return "checksheet/checksheetTemplateForm";
    }

    // 템블릿 수정 화면
    @GetMapping("/checksheetTemplateForm/{templateId}")
    public String templateForm(@PathVariable String templateId, Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        ChecksheetTemplate checksheetTemplate = templateService.getTemplateByCompanyIdAndTemplateId(companyId,
                templateId);

        model.addAttribute("checksheetTemplate", checksheetTemplate);
        return "checksheet/checksheetTemplateForm";
    }

    // 템플릿 저장
    @PostMapping("/checksheetTemplateSave")
    public String saveTemplate(@RequestParam String templateName,
            @RequestParam String templateJson,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        ChecksheetTemplate template = new ChecksheetTemplate();
        template.setTemplateId(templateService.generateTemplateId());
        template.setCompanyId(companyId);
        template.setTemplateName(templateName);
        template.setTemplateJson(templateJson);
        template.setCreateBy(username);
        template.setCreateDate(LocalDateTime.now());

        templateService.saveTemplate(template);
        return "redirect:/checksheet/checksheetTemplateList";
    }

    // 템플릿 목록 조회 화면
    @GetMapping("/checksheetTemplateList")
    public String templateList(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<ChecksheetTemplate> list = templateService.getTemplatesByCompanyId(companyId);
        model.addAttribute("checksheetTemplateList", list);
        return "checksheet/checksheetTemplateList";
    }

    // 템블릿 삭제 화면
    @GetMapping("/checksheetTemplateDelete/{templateId}")
    public String templateDelete(@PathVariable String templateId, Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        try {
            templateService.deleteTemplate(companyId, templateId);
        } catch (Exception e) {
            throw new RuntimeException("템플릿 삭제 중 오류 발생: " + e.getMessage());
        }
        return "checksheet/checksheetTemplateList";
    }

    // Ajax or formRender용 JSON map (선택)
    @GetMapping("/checksheetTemplateMap")
    @ResponseBody
    public Map<String, Object> getTemplatesAsMap(HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        List<ChecksheetTemplate> list = templateService.getTemplatesByCompanyId(companyId);

        Map<String, String> jsonMap = new LinkedHashMap<>();
        for (ChecksheetTemplate tpl : list) {
            jsonMap.put(tpl.getTemplateId(), tpl.getTemplateJson());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("templateList", list);
        result.put("templateJsonMap", jsonMap);
        return result;
    }
}
