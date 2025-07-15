package com.cmms10.inventoryMaster.service;

import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.inventoryMaster.entity.InventoryStock;
import com.cmms10.inventoryMaster.repository.InventoryHistoryRepository;
import com.cmms10.inventoryMaster.repository.InventoryStockRepository;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * cmms10 - InventoryIoService
 * 재고 입출고 처리 서비스 (Pessimistic Lock 적용)
 * 
 * @author cmms10
 * @since 2024-07-13
 */
@Service
public class InventoryIoService {
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final InventoryStockRepository inventoryStockRepository;

    public InventoryIoService(InventoryHistoryRepository inventoryHistoryRepository, InventoryStockRepository inventoryStockRepository) {
        this.inventoryHistoryRepository = inventoryHistoryRepository;
        this.inventoryStockRepository = inventoryStockRepository;
    }

    /**
     * 재고 입출고 처리 (Pessimistic Lock 적용)
     * @param ioList 입출고 내역 리스트
     * @param username 처리자
     */
    @Transactional
    public void processInventoryIo(List<InventoryHistory> ioList, String username) {
        for (InventoryHistory io : ioList) {
            try {
                // 재고(PK: companyId, siteId, locId, inventoryId) 행을 Pessimistic Lock으로 조회
                InventoryStock stock = inventoryStockRepository.findWithLock(
                        io.getCompanyId(), io.getSiteId(), io.getLocId(), io.getInventoryId())
                        .orElseThrow(() -> new RuntimeException("재고 정보가 없습니다."));

                // 수량 계산
                BigDecimal qty = stock.getQty() == null ? BigDecimal.ZERO : stock.getQty();
                BigDecimal ioQty = io.getQty() == null ? BigDecimal.ZERO : io.getQty();
                if ("I".equalsIgnoreCase(io.getIoType())) {
                    qty = qty.add(ioQty);
                } else if ("O".equalsIgnoreCase(io.getIoType())) {
                    qty = qty.subtract(ioQty);
                }
                stock.setQty(qty);
                stock.setUnitPrice(io.getUnitPrice());
                stock.setTotalValue(qty.multiply(io.getUnitPrice() == null ? BigDecimal.ZERO : io.getUnitPrice()));
                inventoryStockRepository.save(stock);

                // 입출고 이력 저장
                io.setCreateDate(LocalDateTime.now());
                io.setCreateBy(username);
                inventoryHistoryRepository.save(io);

            } catch (CannotAcquireLockException e) {
                throw new CannotAcquireLockException("다른 사용자가 처리 중입니다. 잠시만 기다려주세요.");
            }
        }
    }

    /**
     * 재고 입출고 이력 조회
     */
    @Transactional(readOnly = true)
    public List<InventoryHistory> getInventoryHistory(String companyId, String inventoryId) {
        return inventoryHistoryRepository.findByCompanyIdAndInventoryIdOrderByIoDateDesc(companyId, inventoryId).getContent();
    }
} 