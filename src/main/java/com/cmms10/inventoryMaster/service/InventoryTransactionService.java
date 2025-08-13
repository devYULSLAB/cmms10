package com.cmms10.inventoryMaster.service;

import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.inventoryMaster.entity.InventoryMaster;
import com.cmms10.inventoryMaster.entity.InventoryStock;
import com.cmms10.inventoryMaster.repository.InventoryHistoryRepository;
import com.cmms10.inventoryMaster.repository.InventoryMasterRepository;
import com.cmms10.inventoryMaster.repository.InventoryStockRepository;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

/**
 * cmms10 - InventoryTransactionService
 * 재고 거래 처리 서비스
 * 
 * @author cmms10
 * @since 2024-08-04
 */

@Service
public class InventoryTransactionService {

    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final InventoryMasterRepository inventoryMasterRepository;
    private final InventoryStockRepository inventoryStockRepository;

    public InventoryTransactionService(InventoryHistoryRepository inventoryHistoryRepository,
            InventoryMasterRepository inventoryMasterRepository,
            InventoryStockRepository inventoryStockRepository) {
        this.inventoryHistoryRepository = inventoryHistoryRepository;
        this.inventoryMasterRepository = inventoryMasterRepository;
        this.inventoryStockRepository = inventoryStockRepository;
    }

    /**
     * 재고 입출고 처리
     * 
     * @param inventoryHistoryList 재고 거래 리스트
     * @param username             사용자명
     */
    @Transactional
    public void processInventoryIo(List<InventoryHistory> inventoryHistoryList, String username) {
        for (InventoryHistory history : inventoryHistoryList) {
            // 재고 마스터 조회
            InventoryMaster inventoryMaster = inventoryMasterRepository
                    .findByCompanyIdAndInventoryIdAndDeleteMarkIsNull(history.getCompanyId(), history.getInventoryId())
                    .orElseThrow(() -> new RuntimeException("재고 마스터를 찾을 수 없습니다: " + history.getInventoryId()));

            // historyId 생성
            String maxHistoryId = inventoryHistoryRepository.findMaxHistoryIdByCompanyId(history.getCompanyId());
            String newHistoryId = generateNextId(maxHistoryId, "H");
            history.setHistoryId(newHistoryId);

            // 입출고일자 설정 (없으면 오늘 날짜)
            if (history.getIoDate() == null) {
                history.setIoDate(LocalDateTime.now());
            }

            // 총액 계산
            if (history.getQty() != null && history.getUnitPrice() != null) {
                history.setTotalValue(history.getQty().multiply(history.getUnitPrice()));
            }

            // 생성자 설정
            history.setCreateBy(username);

            // 재고 이력 저장
            inventoryHistoryRepository.save(history);

            // 재고 수량 업데이트
            updateInventoryStock(history);
        }
    }

    /**
     * 재고 수량 업데이트
     * 
     * @param history 재고 거래 이력
     */
    private void updateInventoryStock(InventoryHistory history) {
        // 재고 수량 조회 (locId도 포함)
        InventoryStock stock = inventoryStockRepository
                .findByCompanyIdAndSiteIdAndLocIdAndInventoryId(history.getCompanyId(), history.getSiteId(),
                        history.getLocId(), history.getInventoryId())
                .orElse(null);

        if (stock == null) {
            // 새로운 재고 수량 레코드 생성
            stock = new InventoryStock();
            stock.setCompanyId(history.getCompanyId());
            stock.setSiteId(history.getSiteId());
            stock.setLocId(history.getLocId());
            stock.setInventoryId(history.getInventoryId());
            stock.setQty(BigDecimal.ZERO);
        }

        // 입출고에 따른 수량 조정
        if ("I".equals(history.getIoType())) {
            // 입고
            stock.setQty(stock.getQty().add(history.getQty()));
            // 입고 시 단가 정보 업데이트 (최신 단가로 갱신)
            if (history.getUnitPrice() != null) {
                stock.setUnitPrice(history.getUnitPrice());
                stock.setTotalValue(stock.getQty().multiply(history.getUnitPrice()));
            }
        } else if ("O".equals(history.getIoType())) {
            // 출고
            if (stock.getQty().compareTo(history.getQty()) < 0) {
                throw new RuntimeException("재고가 부족합니다. 현재 수량: " + stock.getQty() + ", 요청 수량: " + history.getQty());
            }
            stock.setQty(stock.getQty().subtract(history.getQty()));
            // 출고 후 총액 재계산
            if (stock.getUnitPrice() != null) {
                stock.setTotalValue(stock.getQty().multiply(stock.getUnitPrice()));
            }
        }

        // 재고 수량 저장
        inventoryStockRepository.save(stock);
    }

    /**
     * 재고 이력 조회
     * 
     * @param companyId   회사 ID
     * @param inventoryId 재고 ID (null이면 전체 조회)
     * @return 재고 이력 리스트
     */
    public List<InventoryHistory> getInventoryHistory(String companyId, String inventoryId) {
        if (inventoryId != null && !inventoryId.trim().isEmpty()) {
            return inventoryHistoryRepository.findByCompanyIdAndInventoryIdOrderByIoDateDesc(companyId, inventoryId);
        } else {
            return inventoryHistoryRepository.findByCompanyIdOrderByIoDateDesc(companyId);
        }
    }

    /**
     * 재고 수량 조회
     * 
     * @param companyId   회사 ID
     * @param siteId      사이트 ID
     * @param inventoryId 재고 ID
     * @return 재고 수량
     */
    public BigDecimal getInventoryStock(String companyId, String siteId, String locId, String inventoryId) {
        return inventoryStockRepository
                .findByCompanyIdAndSiteIdAndLocIdAndInventoryId(companyId, siteId, locId, inventoryId)
                .map(InventoryStock::getQty)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 다음 ID 생성
     * 
     * @param maxId  현재 최대 ID
     * @param prefix ID 접두사
     * @return 다음 ID
     */
    private String generateNextId(String maxId, String prefix) {
        if (maxId == null || maxId.isEmpty()) {
            return prefix + "00001";
        }

        try {
            String numericPart = maxId.substring(prefix.length());
            int nextNumber = Integer.parseInt(numericPart) + 1;
            return prefix + String.format("%05d", nextNumber);
        } catch (Exception e) {
            return prefix + "00001";
        }
    }
}