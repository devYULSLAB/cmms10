package com.cmms10.plantMaster.controller;

import com.cmms10.plantMaster.entity.PlantMaster;
import com.cmms10.plantMaster.service.PlantMasterService;
import com.cmms10.domain.site.service.SiteService;
import com.cmms10.funcMaster.service.FuncMasterService;
import com.cmms10.domain.dept.service.DeptService;
import com.cmms10.commonCode.service.CommonCodeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * cmms10 - PlantMasterController
 * 설비 관리 컨트롤러
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Controller
@RequestMapping("/plantMaster") // Base path is correct
public class PlantMasterController {

    private final PlantMasterService plantMasterService;
    private final SiteService siteService;
    private final FuncMasterService funcMasterService;
    private final DeptService deptService;
    private final CommonCodeService commonCodeService;

    public PlantMasterController(PlantMasterService plantMasterService,
            SiteService siteService,
            FuncMasterService funcMasterService,
            DeptService deptService,
            CommonCodeService commonCodeService) {
        this.plantMasterService = plantMasterService;
        this.siteService = siteService;
        this.funcMasterService = funcMasterService;
        this.deptService = deptService;
        this.commonCodeService = commonCodeService;
    }

    /**
     * 설비 등록 화면
     * 
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/plantMasterForm") // Form path is correct
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        PlantMaster plantMaster = new PlantMaster();
        plantMaster.setCompanyId(companyId);

        model.addAttribute("plantMaster", plantMaster);
        // Select box 데이터 추가
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("funcMasters", funcMasterService.getAllFuncMastersByCompanyId(companyId));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));
        model.addAttribute("assetTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "ASSET"));
        model.addAttribute("depreMethods", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "DEPRE"));

        return "plantMaster/plantMasterForm";
    }

    /**
     * 설비 수정 화면
     * 
     * @param plantId
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/plantMasterForm/{siteId}/{plantId}")
    public String editForm(@PathVariable String siteId, @PathVariable String plantId,
            Model model,
            HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        PlantMaster plantMaster = plantMasterService.getPlantMasterByCompanyIdAndSiteIdAndPlantId(companyId, siteId,
                plantId);

        // Select box 데이터 추가
        model.addAttribute("plantMaster", plantMaster);
        model.addAttribute("sites", siteService.getAllSitesByCompanyId(companyId));
        model.addAttribute("funcMasters", funcMasterService.getAllFuncMastersByCompanyId(companyId));
        model.addAttribute("depts", deptService.getAllDeptsByCompanyId(companyId));
        model.addAttribute("assetTypes", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "ASSET"));
        model.addAttribute("depreMethods", commonCodeService.getCommonCodesByCompanyIdAndCodeType(companyId, "DEPRE"));

        return "plantMaster/plantMasterForm";
    }

    /**
     * 설비 등록 화면을 표시합니다.
     * 
     * @return 설비 등록 폼
     */
    @PostMapping("/plantMasterSave")
    public String save(@ModelAttribute PlantMaster plantMaster,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        plantMaster.setCompanyId(companyId);

        plantMasterService.savePlantMaster(plantMaster, username);

        return "redirect:/plantMaster/plantMasterList";
    }

    /**
     * 설비 목록 화면을 조회합니다.
     * 
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

        Page<PlantMaster> plants = plantMasterService.getAllPlantMastersByCompanyId(companyId, pageable);
        model.addAttribute("plants", plants);

        return "plantMaster/plantMasterList";
    }

    /**
     * 설비 상세 화면
     * 
     * @param siteId
     * @param plantId
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/plantMasterDetail/{siteId}/{plantId}")
    public String detail(@PathVariable String siteId, @PathVariable String plantId,
            HttpSession session,
            Model model) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        PlantMaster plantMaster = plantMasterService.getPlantMasterByCompanyIdAndSiteIdAndPlantId(companyId, siteId,
                plantId);
        model.addAttribute("plantMaster", plantMaster);

        return "plantMaster/plantMasterDetail";
    }

    /**
     * 설비 삭제
     * 
     * @param siteId
     * @param plantId
     * @param session
     * @param redirectAttributes
     * @return
     */

    @PostMapping("/plantMasterDelete/{siteId}/{plantId}")
    public String delete(@PathVariable String siteId, @PathVariable String plantId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        try {
            plantMasterService.deletePlantMaster(companyId, siteId, plantId, username);
        } catch (Exception e) {
            throw new RuntimeException("삭제 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/plantMaster/plantMasterList";
    }
}