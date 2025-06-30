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
    public String form(@RequestParam(value = "plantId", required = false) String plantId,
                   Model model, 
                   HttpSession session, 
                   RedirectAttributes redirectAttributes) {
        String companyId = (String) session.getAttribute("companyId");
        String siteId = (String) session.getAttribute("siteId");

        PlantMaster plantMaster;
        if (plantId != null && !plantId.isEmpty()) {
            // 편집(수정) 화면: plantId로 기존 데이터 조회
            Optional<PlantMaster> plantOpt = plantMasterService.getPlantMasterByplantId(companyId, plantId);
            if (plantOpt.isPresent()) {
                plantMaster = plantOpt.get();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "해당 설비를 찾을 수 없습니다.");
                return "redirect:/plantMaster/plantMasterList";
            }
        } else {
            // 신규 등록 화면: 빈 객체 전달
            plantMaster = new PlantMaster();
        }

        model.addAttribute("plantMaster", plantMaster);
        model.addAttribute("companyId", companyId);
        model.addAttribute("siteId", siteId);
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

        if (companyId == null || siteId == null || username == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Session expired. Please log in again.");
            return "redirect:/login";
        }

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
        
        Optional<PlantMaster> plantMasterOpt = plantMasterService.getPlantMasterByplantId(companyId, plantId);
        if (plantMasterOpt.isPresent()) {
            model.addAttribute("plantMaster", plantMasterOpt.get());
            return "plantMaster/plantMasterDetail";
        } else {
            model.addAttribute("errorMessage", "Plant Master not found with ID: " + plantId);
            return "plantMaster/plantMasterList";
        }
    }

    

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