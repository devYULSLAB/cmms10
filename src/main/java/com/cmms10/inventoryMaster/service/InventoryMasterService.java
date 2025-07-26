package com.cmms10.inventoryMaster.service;

import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryMasterIdClass;
import com.cmms10.inventoryMaster.repository.InventoryMasterRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * cmms10 - InventoryService
 * 재고 관리 서비스
 * 
 * @author cmms10
 * @since 2024-03-19
 */

@Service
public class InventoryMasterService {

    private final InventoryMasterRepository inventoryMasterRepository;

    public InventoryMasterService(InventoryMasterRepository inventoryMasterRepository) {
        this.inventoryMasterRepository = inventoryMasterRepository;
    }

    @Transactional(readOnly = true)
    public Page<InventoryMaster> getAllInventoryMasters(String companyId, Pageable pageable) {
        return inventoryMasterRepository.findByCompanyIdAndDeleteMarkIsNull(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public InventoryMaster getInventoryMasterByCompanyIdAndInventoryId(String companyId, String inventoryId) {
        return inventoryMasterRepository.findByCompanyIdAndInventoryIdAndDeleteMarkIsNull(companyId, inventoryId)
                .orElseThrow(() -> new RuntimeException("InventoryMaster not found: " + inventoryId));
    }

    @Transactional
    public InventoryMaster saveInventoryMaster(InventoryMaster inventoryMaster, String username) {
        LocalDateTime now = LocalDateTime.now();

        boolean isNewInventoryMaster = (inventoryMaster.getInventoryId() == null
                || inventoryMaster.getInventoryId().isEmpty());

        if (isNewInventoryMaster) {
            String maxInventoryId = inventoryMasterRepository
                    .findMaxInventoryIdByCompanyId(inventoryMaster.getCompanyId());
            String newInventoryId = (maxInventoryId == null) ? "2000000000"
                    : String.valueOf(Long.parseLong(maxInventoryId) + 1);

            inventoryMaster.setInventoryId(newInventoryId);
            inventoryMaster.setCreateDate(now);
            inventoryMaster.setCreateBy(username);
        } else {
            inventoryMaster.setUpdateDate(now);
            inventoryMaster.setUpdateBy(username);
        }

        return inventoryMasterRepository.save(inventoryMaster);
    }

    @Transactional
    public void deleteInventoryMaster(String companyId, String inventoryId) {
        InventoryMasterIdClass id = new InventoryMasterIdClass(companyId, inventoryId);
        if (!inventoryMasterRepository.existsById(id)) {
            throw new RuntimeException("InventoryMaster not found with id: " + id);
        }
        inventoryMasterRepository.deleteById(id);
    }

}