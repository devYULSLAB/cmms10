package com.cmms10.inventoryMaster.service;

import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.inventoryMaster.entity.InventoryStock;
import com.cmms10.inventoryMaster.repository.InventoryMasterRepository;
import com.cmms10.inventoryMaster.repository.InventoryHistoryRepository;
import com.cmms10.inventoryMaster.repository.InventoryStockRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    private final InventoryStockRepository inventoryStockRepository;

    public InventoryMasterService(InventoryMasterRepository inventoryMasterRepository,
            InventoryHistoryRepository inventoryHistoryRepository,
            InventoryStockRepository inventoryStockRepository) {
        this.inventoryMasterRepository = inventoryMasterRepository;
        this.inventoryHistoryRepository = inventoryHistoryRepository;
        this.inventoryStockRepository = inventoryStockRepository;
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
    public void deleteInventoryMaster(String companyId, String inventoryId, String username) {
        InventoryMaster inventoryMaster = inventoryMasterRepository
                .findByCompanyIdAndInventoryIdAndDeleteMarkIsNull(companyId, inventoryId)
                .orElseThrow(() -> new RuntimeException("InventoryMaster not found: " + inventoryId));

        // 연관 데이터 검증 - 재고 이력이나 재고 수량이 있는지 확인
        if (hasRelatedData(companyId, inventoryId)) {
            throw new RuntimeException("재고 이력이나 재고 수량이 존재하여 삭제할 수 없습니다. 먼저 관련 데이터를 정리해주세요.");
        }

        inventoryMaster.setDeleteMark("Y");
        inventoryMaster.setUpdateDate(LocalDateTime.now());
        inventoryMaster.setUpdateBy(username);

        inventoryMasterRepository.save(inventoryMaster);
    }

    /**
     * 연관 데이터 존재 여부 확인
     * 
     * @param companyId   회사 ID
     * @param inventoryId 재고 ID
     * @return 연관 데이터 존재 여부
     */
    private boolean hasRelatedData(String companyId, String inventoryId) {
        // 재고 이력 확인
        List<InventoryHistory> historyList = inventoryHistoryRepository
                .findByCompanyIdAndInventoryIdOrderByTransactionDateDesc(companyId, inventoryId);
        boolean hasHistory = !historyList.isEmpty();

        // 재고 수량 확인 (모든 사이트/위치에서 검색)
        List<InventoryStock> stockList = inventoryStockRepository.findAll().stream()
                .filter(stock -> stock.getCompanyId().equals(companyId) && stock.getInventoryId().equals(inventoryId))
                .toList();
        boolean hasStock = !stockList.isEmpty();

        return hasHistory || hasStock;
    }

}