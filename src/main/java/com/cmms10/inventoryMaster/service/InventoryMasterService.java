package com.cmms10.inventoryMaster.service;

import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryMasterIdClass;
import com.cmms10.inventoryMaster.repository.InventoryMasterRepository;
import com.cmms10.inventoryMaster.repository.InventoryHistoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final InventoryHistoryRepository inventoryHistoryRepository;

    public InventoryMasterService(InventoryMasterRepository inventoryMasterRepository,
                                  InventoryHistoryRepository inventoryHistoryRepository) {
        this.inventoryMasterRepository = inventoryMasterRepository;
        this.inventoryHistoryRepository = inventoryHistoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<InventoryMaster> getAllInventoryMasters(String companyId, String siteId, Pageable pageable) {
        return inventoryMasterRepository.findByCompanyIdAndSiteIdAndDeleteMarkIsNull(companyId, siteId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<InventoryMaster> getInventoryMasterByInventoryId(String companyId, String inventoryId) {
        return inventoryMasterRepository.findByCompanyIdAndInventoryIdAndDeleteMarkIsNull(companyId, inventoryId);
    }

    @Transactional
    public InventoryMaster saveInventoryMaster(InventoryMaster inventoryMaster, String username) {
        LocalDateTime now = LocalDateTime.now();
        String maxInventoryId = inventoryMasterRepository.findMaxInventoryIdByCompanyId(inventoryMaster.getCompanyId());
        int newInventoryId = (maxInventoryId == null) ? 2000000000 : Integer.parseInt(maxInventoryId) + 1;
        inventoryMaster.setInventoryId(String.valueOf(newInventoryId));
        inventoryMaster.setCreateDate(now);
        inventoryMaster.setCreateBy(username);
        
        return inventoryMasterRepository.save(inventoryMaster);

    }

    @Transactional(readOnly = true)
    public List<InventoryHistory> getInventoryHistory(String companyId, String inventoryId) {
        return inventoryHistoryRepository.findByCompanyIdAndInventoryIdOrderByIoDateDesc(companyId, inventoryId);
    }

    @Transactional
    public void deleteInventoryMaster(String companyId, String inventoryId) {
        InventoryMasterIdClass id = new InventoryMasterIdClass(companyId, inventoryId);
        if (!inventoryMasterRepository.existsById(id)) {
            throw new RuntimeException("InventoryMaster not found with id: " + id);
        }
        inventoryMasterRepository.deleteById(id);
    }

    @Transactional
    public void processInventoryIo(List<InventoryHistory> ioList, String username) {
        for (InventoryHistory dto : ioList) {

            InventoryMaster inv = inventoryMasterRepository
                .findByCompanyIdAndInventoryIdForUpdate(dto.getCompanyId(), dto.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

            BigDecimal qtyChange = "I".equalsIgnoreCase(dto.getIoType()) ? dto.getQty() : dto.getQty().negate();
            BigDecimal currentQty = inv.getCurrentQty() == null ? BigDecimal.ZERO : inv.getCurrentQty();
            BigDecimal newQty = currentQty.add(qtyChange);

            if (newQty.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Insufficient stock for " + dto.getInventoryId());
            }

            inv.setCurrentQty(newQty);

            BigDecimal valueChange = "I".equalsIgnoreCase(dto.getIoType()) ? dto.getTotalValue() : dto.getTotalValue().negate();
            BigDecimal currentValue = inv.getCurrentValue() == null ? BigDecimal.ZERO : inv.getCurrentValue();
            inv.setCurrentValue(currentValue.add(valueChange));
            inv.setUpdateBy(username);
            inv.setUpdateDate(LocalDateTime.now());

            inventoryMasterRepository.save(inv);

            dto.setIoDate(dto.getIoDate() == null ? LocalDateTime.now() : dto.getIoDate());
            dto.setCreateDate(LocalDateTime.now());
            dto.setCreateBy(username);
            if (dto.getTotalValue() == null && dto.getQty() != null && dto.getUnitPrice() != null) {
                dto.setTotalValue(dto.getQty().multiply(dto.getUnitPrice()));
            }
            dto.setHistoryId(generateHistoryId(dto.getCompanyId()));

            inventoryHistoryRepository.save(dto);
        }
    }

    private String generateHistoryId(String companyId) {
        String maxId = inventoryHistoryRepository.findMaxHistoryIdByCompanyId(companyId);
        int newId = (maxId == null) ? 1 : Integer.parseInt(maxId) + 1;
        return String.valueOf(newId);
    }

} 