package com.cmms10.plantMaster.controller;

import com.cmms10.plantMaster.entity.PlantMaster;
import com.cmms10.plantMaster.service.PlantMasterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * cmms10 - PlantMasterController
 * 설비 관리 컨트롤러
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Controller
@RequestMapping("/plantMaster")  // Base path is correct
public class PlantMasterController {

    private final PlantMasterService plantMasterService;

    public PlantMasterController(PlantMasterService plantMasterService) {
        this.plantMasterService = plantMasterService;
    }

    /**
     * 설비 등록 화면
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/plantMasterForm")  // Form path is correct
    public String form(Model model, HttpSession session) {

        PlantMaster plantMaster = new PlantMaster();
        model.addAttribute("plantMaster", plantMaster);
        return "plantMaster/plantMasterForm";
    }

    /**
     * 설비 수정 화면
     * @param plantId
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/plantMasterForm/{plantId}")
    public String form(@PathVariable String plantId,
                       Model model,
                       HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        PlantMaster plantMaster = plantMasterService.getPlantMasterByplantId(companyId, plantId);
        model.addAttribute("plantMaster", plantMaster);
        return "plantMaster/plantMasterForm";        
    }

    /**
     * 설비 등록 화면을 표시합니다.
     * @return 설비 등록 폼
     */
    @PostMapping("/plantMasterSave")
    public String save(@ModelAttribute PlantMaster plantMaster,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        String username = (String) session.getAttribute("username");
        // 필수 정보 설정
        plantMaster.setCompanyId(companyId);
        plantMaster.setSiteId(siteId);

        plantMasterService.savePlantMaster(plantMaster, username);
        
        return "redirect:/plantMaster/plantMasterList";
    }

    /**
     * 설비 목록 화면을 조회합니다.
     * @param model
     * @param session
     * @param pageable
     * @return
     */
    @GetMapping("/plantMasterList")
    public String list(Model model,
                        HttpSession session,
                        @PageableDefault(size = 10, sort = "plantId") Pageable pageable) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");
        
        Page<PlantMaster> plantPage = plantMasterService.getAllPlantMasters(companyId, siteId, pageable);
        model.addAttribute("plantPage", plantPage);

        return "plantMaster/plantMasterList";
    }

    
    /**
     * 설비 상세 화면
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/plantMasterDetail/{plantId}")
    public String detail(@PathVariable String plantId,
                                       HttpSession session,
                                       Model model) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");

        PlantMaster plantMaster = plantMasterService.getPlantMasterByplantId(companyId, plantId);
        model.addAttribute("plantMaster", plantMaster);
        model.addAttribute("companyId", companyId); //페이징에서 사용 
        model.addAttribute("siteId", siteId); //페이징에서 사용 

        return "plantMaster/plantMasterDetail";
    }
    
    /**
     * 설비 삭제
     * @param plantId
     * @param session
     * @param redirectAttributes
     * @return
     */
    

    @PostMapping("/plantMasterDelete/{plantId}")
    public String delete(@PathVariable String plantId,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        try {
            plantMasterService.deletePlantMaster(companyId, plantId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Plant Master '" + plantId + "' deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error deleting Plant Master: " + e.getMessage());
        }
        return "redirect:/plantMaster/plantMasterList";
    }
}