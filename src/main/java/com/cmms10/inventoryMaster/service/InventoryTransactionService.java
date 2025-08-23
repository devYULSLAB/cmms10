package com.cmms10.inventoryMaster.service;

import com.cmms10.inventoryMaster.entity.InventoryHistory;
import com.cmms10.inventoryMaster.entity.InventoryStock;
import com.cmms10.inventoryMaster.entity.InventoryStockId;
import com.cmms10.inventoryMaster.repository.InventoryHistoryRepository;
import com.cmms10.inventoryMaster.repository.InventoryStockRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 재고 트랜잭션 서비스 (avg/평균단가 관련 처리 없음)
 * - 입고: 도착 위치 재고 수량 증가
 * - 출고: 출발 위치 재고 수량 감소(부족하면 예외)
 * - 이동: 출발 위치 수량 감소 + 도착 위치 수량 증가(부족하면 예외)
 * - 조정: toLocation 기준으로 qty 증감(음수 가능 정책)
 *
 * 주의:
 * - 컨트롤러에서 companyId/siteId/transactionType/transactionDate를 세팅해 넘긴다고 가정합니다.
 * - unitPrice/totalValue는 히스토리 기록용으로만 사용하고, 재고수량만 업데이트합니다.
 */
@Service
public class InventoryTransactionService {

    private final InventoryHistoryRepository historyRepo;
    private final InventoryStockRepository stockRepo;

    // 회사별 시퀀스 번호 (동시성 안전)
    private final java.util.concurrent.ConcurrentHashMap<String, AtomicInteger> companySequences = new java.util.concurrent.ConcurrentHashMap<>();

    public InventoryTransactionService(InventoryHistoryRepository historyRepo,
            InventoryStockRepository stockRepo) {
        this.historyRepo = historyRepo;
        this.stockRepo = stockRepo;
    }

    /**
     * 고유한 historyId 생성 (15자 이내)
     * - 회사ID(5자) + 날짜(6자) + 시퀀스(4자)
     * - 연도별로 시퀀스 관리
     * - 연도별 최대 9,999개
     * - 연도별 시퀀스 초기화
     * - 연도별 시퀀스 증가
     * - 연도별 시퀀스 초기화
     * 초기화되면 번호 생성이 중복되는 문제가 있으니 초기화 되지 않도록 처리
     */
    private String generateHistoryId(String companyId) {
        LocalDateTime now = LocalDateTime.now();

        // 연도(2자리) + 월(2자리) + 일(2자리) + 시퀀스(4자리)
        String year = String.format("%02d", now.getYear() % 100); // 25, 26, 27...
        String month = String.format("%02d", now.getMonthValue());
        String day = String.format("%02d", now.getDayOfMonth());

        // 밀리초 기반 고유 번호 (4자리)
        // 현재 밀리초를 4자리로 압축 (0-9999)
        long currentMillis = System.currentTimeMillis();
        String millisStr = String.format("%04d", (currentMillis % 10000));

        // companyId(5자) + year(2자) + month(2자) + day(2자) + millis(4자) = 15자
        return companyId + year + month + day + millisStr;
    }

    // -------------------- 입고 --------------------
    @Transactional
    public void processInbound(List<InventoryHistory> rows, String username) {
        LocalDateTime now = LocalDateTime.now();

        for (InventoryHistory row : rows) {
            // 이력 공통 메타 업데이트
            row.setCreateBy(username);
            row.setCreateDate(now);
            // historyId 자동 생성
            row.setHistoryId(generateHistoryId(row.getCompanyId()));
            // totalValue 계산 (unitPrice × qty)
            row.setTotalValue(row.getQty().multiply(row.getUnitPrice()));

            // 재고 증가 (toLocation)
            InventoryStock stock = loadOrCreateStock(row.getCompanyId(), row.getSiteId(), row.getToLocation(),
                    row.getInventoryId());
            BigDecimal newQty = nz(stock.getQty()).add(nz(row.getQty()));
            stock.setQty(newQty);

            // totalValue 누적 (기존 + 신규)
            BigDecimal newTotalValue = nz(stock.getTotalValue()).add(row.getTotalValue());
            stock.setTotalValue(newTotalValue);

            // UnitPrice 업데이트
            stock.setUnitPrice(newTotalValue.divide(newQty, 2, RoundingMode.HALF_UP));

            stockRepo.save(stock);
            historyRepo.save(row);
        }
    }

    // -------------------- 출고 --------------------
    @Transactional
    public void processOutbound(List<InventoryHistory> rows, String username) {
        LocalDateTime now = LocalDateTime.now();

        for (InventoryHistory row : rows) {
            row.setCreateBy(username);
            row.setCreateDate(now);
            // historyId 자동 생성
            row.setHistoryId(generateHistoryId(row.getCompanyId()));
            // totalValue 계산 (unitPrice × qty)
            row.setTotalValue(row.getQty().multiply(row.getUnitPrice()));

            // 출발 위치 재고 차감
            InventoryStock stock = stockRepo.findById(new InventoryStockId(
                    row.getCompanyId(), row.getSiteId(), row.getFromLocation(), row.getInventoryId()))
                    .orElseThrow(() -> new IllegalStateException(
                            "출고 위치에 재고가 없습니다. inv=" + row.getInventoryId() +
                                    ", loc=" + row.getFromLocation()));

            BigDecimal curQty = nz(stock.getQty());
            BigDecimal outQty = nz(row.getQty());
            BigDecimal newQty = nz(stock.getQty()).subtract(nz(row.getQty()));
            if (newQty.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException(
                        "출고 수량이 재고 수량을 초과합니다. inv=" + row.getInventoryId() +
                                ", loc=" + row.getFromLocation() + ", stock=" + curQty + ", out=" + outQty);
            }

            stock.setQty(newQty);

            // 출고 시에는 기존 unitprice 기준으로 totalValue 계산
            // 재고가 0이 되면 totalValue도 0으로 설정
            if (newQty.compareTo(BigDecimal.ZERO) == 0) {
                stock.setTotalValue(BigDecimal.ZERO);
            } else {
                stock.setTotalValue(newQty.multiply(stock.getUnitPrice()));
            }

            stockRepo.save(stock);
            historyRepo.save(row);
        }
    }

    // -------------------- 이동 --------------------
    @Transactional
    public void processMovement(List<InventoryHistory> rows, String username) {
        LocalDateTime now = LocalDateTime.now();

        for (InventoryHistory row : rows) {
            row.setCreateBy(username);
            row.setCreateDate(now);
            // historyId 자동 생성
            row.setHistoryId(generateHistoryId(row.getCompanyId()));
            // totalValue 계산 (unitPrice × qty)
            row.setTotalValue(row.getQty().multiply(row.getUnitPrice()));

            BigDecimal mvQty = nz(row.getQty());

            // 1) 출발 위치 차감
            InventoryStock from = stockRepo.findById(new InventoryStockId(
                    row.getCompanyId(), row.getSiteId(), row.getFromLocation(), row.getInventoryId()))
                    .orElseThrow(() -> new IllegalStateException(
                            "이동 출발 위치에 재고가 없습니다. inv=" + row.getInventoryId() +
                                    ", from=" + row.getFromLocation()));

            BigDecimal fromQty = nz(from.getQty());
            if (fromQty.compareTo(mvQty) < 0) {
                throw new IllegalStateException(
                        "이동 수량이 출발지 재고를 초과합니다. inv=" + row.getInventoryId() +
                                ", from=" + row.getFromLocation() + ", stock=" + fromQty + ", move=" + mvQty);
            }

            BigDecimal newFromQty = fromQty.subtract(mvQty);
            from.setQty(newFromQty);

            // 출발 위치는 기존 unitPrice 유지, totalValue만 재계산
            if (newFromQty.compareTo(BigDecimal.ZERO) == 0) {
                from.setTotalValue(BigDecimal.ZERO);
            } else {
                from.setTotalValue(newFromQty.multiply(from.getUnitPrice()));
            }

            stockRepo.save(from);

            // 2) 도착 위치 가산
            InventoryStock to = loadOrCreateStock(row.getCompanyId(), row.getSiteId(), row.getToLocation(),
                    row.getInventoryId());

            BigDecimal newToQty = nz(to.getQty()).add(mvQty);
            to.setQty(newToQty);

            // 도착 위치는 기존 totalValue + 이동 totalValue로 누적
            BigDecimal newToTotalValue = nz(to.getTotalValue()).add(row.getTotalValue());
            to.setTotalValue(newToTotalValue);

            // 도착 위치의 unitPrice는 평균 계산 (총액 ÷ 총수량)
            if (newToQty.compareTo(BigDecimal.ZERO) > 0) {
                to.setUnitPrice(newToTotalValue.divide(newToQty, 2, RoundingMode.HALF_UP));
            }

            stockRepo.save(to);

            // 이력
            historyRepo.save(row);
        }
    }

    // -------------------- 조정 --------------------
    /**
     * 조정 정책:
     * - toLocation 기준으로 qty 만큼 증감 (qty가 음수면 감소로 취급)
     * - 감소 시 0 미만으로 내려가면 예외
     */
    @Transactional
    public void processAdjustment(List<InventoryHistory> rows, String username) {
        LocalDateTime now = LocalDateTime.now();

        for (InventoryHistory row : rows) {
            row.setCreateBy(username);
            row.setCreateDate(now);
            // historyId 자동 생성
            row.setHistoryId(generateHistoryId(row.getCompanyId()));
            // totalValue 계산 (unitPrice × qty)
            row.setTotalValue(row.getQty().multiply(row.getUnitPrice()));

            BigDecimal adjustQty = nz(row.getQty());
            InventoryStock stock = loadOrCreateStock(row.getCompanyId(), row.getSiteId(), row.getToLocation(),
                    row.getInventoryId());

            BigDecimal newQty = nz(stock.getQty()).add(adjustQty);
            if (newQty.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException(
                        "조정 후 재고가 음수가 됩니다. inv=" + row.getInventoryId() +
                                ", loc=" + row.getToLocation() + ", current=" + stock.getQty() +
                                ", adjust=" + adjustQty + ", result=" + newQty);
            }

            stock.setQty(newQty);

            // totalValue 누적 (기존 + 신규)
            BigDecimal newTotalValue = nz(stock.getTotalValue()).add(row.getTotalValue());
            stock.setTotalValue(newTotalValue);

            stockRepo.save(stock);
            historyRepo.save(row);
        }
    }

    // -------------------- helpers --------------------
    private InventoryStock loadOrCreateStock(String companyId, String siteId, String locId, String inventoryId) {
        try {
            // Pessimistic Lock으로 동시 접근 차단
            Optional<InventoryStock> existingStock = stockRepo.findWithLock(companyId, siteId, locId, inventoryId);

            if (existingStock.isPresent()) {
                return existingStock.get();
            }

            // Lock을 유지한 상태에서 새로 생성
            InventoryStock s = new InventoryStock();
            s.setCompanyId(companyId);
            s.setSiteId(siteId);
            s.setInventoryId(inventoryId);
            s.setLocId(locId);
            s.setQty(BigDecimal.ZERO);
            s.setUnitPrice(BigDecimal.ZERO);
            s.setTotalValue(BigDecimal.ZERO);

            return s;

        } catch (Exception e) {
            // Lock 획득 실패 시 재시도 로직
            throw new IllegalStateException("Lock 획득 실패, 재시도: " + e.getMessage());
        }
    }

    public BigDecimal getUnitPrice(String companyId, String siteId, String locId, String inventoryId) {
        return stockRepo.findByCompanyIdAndSiteIdAndLocIdAndInventoryId(companyId, siteId, locId, inventoryId)
                .map(InventoryStock::getUnitPrice)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
