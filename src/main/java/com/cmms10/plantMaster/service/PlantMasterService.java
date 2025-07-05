package com.cmms10.plantMaster.service;

import com.cmms10.plantMaster.entity.PlantMaster;
import com.cmms10.plantMaster.entity.PlantMasterIdClass;
import com.cmms10.plantMaster.repository.PlantMasterRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * cmms10 - PlantService
 * 설비 관리 서비스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Service
public class PlantMasterService {

    private final PlantMasterRepository plantMasterRepository;
      
    public PlantMasterService(PlantMasterRepository plantMasterRepository) {
        this.plantMasterRepository = plantMasterRepository;
    }

    @Transactional(readOnly = true)
    public Page<PlantMaster> getAllPlantMasters(String companyId, String siteId, Pageable pageable) {
        return plantMasterRepository.findByCompanyIdAndSiteIdAndDeleteMarkIsNull(companyId, siteId, pageable);
    }

    @Transactional(readOnly = true)
    public PlantMaster getPlantMasterByplantId(String companyId, String plantId) {
        return plantMasterRepository.findByCompanyIdAndPlantIdAndDeleteMarkIsNull(companyId, plantId)
                .orElseThrow(() -> new RuntimeException("PlantMaster not found: " + plantId));
    }

    @Transactional
    public PlantMaster savePlantMaster(PlantMaster plantMaster, String username) {
        LocalDateTime now = LocalDateTime.now();
        if(plantMaster.getPlantId() == null || plantMaster.getPlantId().isEmpty()) {
            String maxPlantId = plantMasterRepository.findMaxPlantIdByCompanyId(plantMaster.getCompanyId());
            Long newPlantId = (maxPlantId == null) ? 1000000000L : Long.parseLong(maxPlantId) + 1;
            plantMaster.setPlantId(String.valueOf(newPlantId));
            plantMaster.setCreateDate(now);
            plantMaster.setCreateBy(username);            
        } else {
            // 수정정
            plantMaster.setUpdateDate(now);
            plantMaster.setUpdateBy(username);
        }   
                
        return plantMasterRepository.save(plantMaster);
    }

    @Transactional
    public void deletePlantMaster(String companyId, String plantId) {
        PlantMasterIdClass id = new PlantMasterIdClass(companyId, plantId);
        if (!plantMasterRepository.existsById(id)) {
            throw new RuntimeException("PlantMaster not found with id: " + id);
        }
        plantMasterRepository.deleteById(id);
    }
}