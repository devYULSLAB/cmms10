/**
 * CMMS10 - 작업지시 관리 서비스
 * 
 * 주요 기능:
 * - 작업지시 목록 조회 (페이징, 검색 조건별)
 * - 작업지시 상세 조회
 * - 작업지시 저장/수정
 * - 작업지시 삭제
 * - 작업지시 항목 관리
 * 
 * 생성자: 로그인 사용자
 * 생성일: 2024-01-01
 */
package com.cmms10.workorder.service;

import com.cmms10.workorder.entity.Workorder;
import com.cmms10.workorder.entity.WorkorderItem;
import com.cmms10.workorder.repository.WorkorderRepository;
import com.cmms10.workorder.repository.WorkorderItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WorkorderService {
    private final WorkorderRepository workorderRepository;
    private final WorkorderItemRepository workorderItemRepository;

    public WorkorderService(
            WorkorderRepository workorderRepository,
            WorkorderItemRepository workorderItemRepository) {
        this.workorderRepository = workorderRepository;
        this.workorderItemRepository = workorderItemRepository;
    }

    /**
     * 회사 ID별 전체 작업지시 목록을 페이징하여 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param pageable  페이징 정보 (페이지 번호, 크기, 정렬)
     * @return Page<Workorder> 작업지시 목록 (페이징 정보 포함)
     */
    @Transactional(readOnly = true)
    public Page<Workorder> getAllWorkordersByCompanyId(String companyId, Pageable pageable) {
        return workorderRepository.findByCompanyIdOrderByOrderIdAsc(companyId, pageable);
    }

    /**
     * 회사 ID와 사이트 ID별 작업지시 목록을 페이징하여 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @param pageable  페이징 정보 (페이지 번호, 크기, 정렬)
     * @return Page<Workorder> 특정 사이트의 작업지시 목록 (페이징 정보 포함)
     */
    @Transactional(readOnly = true)
    public Page<Workorder> getWorkordersByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable) {
        return workorderRepository.findByCompanyIdAndSiteIdOrderByOrderIdAsc(companyId, siteId, pageable);
    }

    /**
     * 회사 ID, 사이트 ID, 설비 ID별 작업지시 목록을 페이징하여 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @param plantId   설비 ID
     * @param pageable  페이징 정보 (페이지 번호, 크기, 정렬)
     * @return Page<Workorder> 특정 설비의 작업지시 목록 (페이징 정보 포함)
     */
    @Transactional(readOnly = true)
    public Page<Workorder> getWorkordersByCompanyIdAndSiteIdAndPlantId(String companyId, String siteId, String plantId,
            Pageable pageable) {
        return workorderRepository.findByCompanyIdAndSiteIdAndPlantIdOrderByOrderIdAsc(companyId, siteId, plantId,
                pageable);
    }

    /**
     * 회사 ID와 설비 ID별 작업지시 목록을 페이징하여 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param plantId   설비 ID
     * @param pageable  페이징 정보 (페이지 번호, 크기, 정렬)
     * @return Page<Workorder> 특정 설비의 작업지시 목록 (페이징 정보 포함)
     */
    @Transactional(readOnly = true)
    public Page<Workorder> getWorkordersByCompanyIdAndPlantId(String companyId, String plantId,
            Pageable pageable) {
        return workorderRepository.findByCompanyIdAndPlantIdOrderByOrderIdAsc(companyId, plantId,
                pageable);
    }

    /**
     * 회사 ID, 사이트 ID, 작업지시 ID로 특정 작업지시를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @param orderId   작업지시 ID
     * @return Workorder 작업지시 엔티티
     * @throws IllegalArgumentException 작업지시를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Workorder getWorkorderByCompanyIdAndSiteIdAndOrderId(String companyId, String siteId, String orderId) {
        return workorderRepository.findByCompanyIdAndSiteIdAndOrderId(companyId, siteId, orderId)
                .orElseThrow(() -> new IllegalArgumentException("작업지시를 찾을 수 없습니다: " + orderId));
    }

    /**
     * 회사 ID와 작업지시 ID로 작업지시 항목 목록을 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param orderId   작업지시 ID
     * @return List<WorkorderItem> 작업지시 항목 목록
     */
    @Transactional(readOnly = true)
    public List<WorkorderItem> getWorkorderItems(String companyId, String orderId) {
        return workorderItemRepository.findByCompanyIdAndOrderIdOrderByItemIdAsc(companyId, orderId);
    }

    /**
     * 작업지시를 저장하거나 수정합니다.
     * 신규 작업지시인 경우 자동으로 작업지시 ID를 생성하고, 기존 작업지시인 경우 수정 정보를 업데이트합니다.
     * 작업지시 항목들도 함께 처리하여 유효한 항목만 저장합니다.
     * 
     * @param workorder 저장할 작업지시 엔티티
     * @param username  작업을 수행한 사용자명
     * @return Workorder 저장된 작업지시 엔티티
     */
    @Transactional
    public Workorder saveWorkorder(Workorder workorder, String username) {
        LocalDateTime now = LocalDateTime.now();
        boolean isNewWorkorder = (workorder.getOrderId() == null || workorder.getOrderId().isEmpty());

        if (isNewWorkorder) {
            String maxOrderId = workorderRepository.findMaxOrderIdByCompanyId(workorder.getCompanyId());
            String newOrderId = (maxOrderId == null) ? "5000000000" : String.valueOf(Long.parseLong(maxOrderId) + 1);

            workorder.setOrderId(newOrderId);
            workorder.setCreateDate(now);
            workorder.setCreateBy(username);
        } else {
            workorder.setUpdateDate(now);
            workorder.setUpdateBy(username);
        }

        // WorkorderItem 처리
        List<WorkorderItem> items = workorder.getItems();
        if (items != null && !items.isEmpty()) {
            // 새로운 리스트를 생성하여 유효한 항목만 추가
            List<WorkorderItem> validItems = new ArrayList<>();
            int itemIndex = 1;

            for (WorkorderItem item : items) {
                if (item.getItemName() != null && !item.getItemName().isEmpty()) {
                    item.setCompanyId(workorder.getCompanyId());
                    item.setOrderId(workorder.getOrderId());
                    item.setItemId(String.format("%02d", itemIndex++));
                    item.setWorkorder(workorder);
                    validItems.add(item);
                }
            }

            // 기존 리스트를 비우고 유효한 항목들로 교체
            items.clear();
            items.addAll(validItems);
        }

        return workorderRepository.save(workorder);
    }

    /**
     * 작업지시 항목을 저장합니다.
     * 신규 항목인 경우 자동으로 항목 ID를 생성합니다.
     * 
     * @param workorderItem 저장할 작업지시 항목 엔티티
     * @param username      작업을 수행한 사용자명
     * @return WorkorderItem 저장된 작업지시 항목 엔티티
     */
    @Transactional
    public WorkorderItem saveWorkorderItem(WorkorderItem workorderItem, String username) {

        String maxItemId = workorderItemRepository.findMaxItemIdByCompanyIdAndOrderId(
                workorderItem.getCompanyId(),
                workorderItem.getOrderId());
        int newItemId = (maxItemId == null) ? 1 : Integer.parseInt(maxItemId) + 1;
        workorderItem.setItemId(String.valueOf(newItemId));

        return workorderItemRepository.save(workorderItem);
    }

    /**
     * 작업지시를 삭제합니다.
     * 작업지시와 연관된 모든 항목들도 함께 삭제됩니다.
     * 
     * @param companyId   회사 ID
     * @param siteId      사이트 ID
     * @param workorderId 작업지시 ID
     * @throws RuntimeException 작업지시를 찾을 수 없는 경우
     */
    @Transactional
    public void deleteWorkorder(String companyId, String siteId, String workorderId) {
        Optional<Workorder> workorderOpt = workorderRepository.findByCompanyIdAndSiteIdAndOrderId(
                companyId,
                siteId,
                workorderId);

        if (workorderOpt.isPresent()) {
            Workorder workorder = workorderOpt.get();
            // Delete associated items first
            List<WorkorderItem> items = workorderItemRepository.findByCompanyIdAndOrderIdOrderByItemIdAsc(companyId,
                    workorderId);
            workorderItemRepository.deleteAll(items);
            // Delete the work order
            workorderRepository.delete(workorder);
        } else {
            throw new RuntimeException("workorder not found with ID: " + workorderId);
        }
    }

    /**
     * 작업지시 항목을 삭제합니다.
     * 
     * @param companyId   회사 ID
     * @param workorderId 작업지시 ID
     * @param itemId      항목 ID
     * @throws RuntimeException 작업지시 항목을 찾을 수 없는 경우
     */
    @Transactional
    public void deleteWorkorderItem(String companyId, String workorderId, String itemId) {
        Optional<WorkorderItem> itemOpt = workorderItemRepository.findByCompanyIdAndOrderIdAndItemId(
                companyId,
                workorderId,
                itemId);

        if (itemOpt.isPresent()) {
            WorkorderItem item = itemOpt.get();
            workorderItemRepository.delete(item);
        } else {
            throw new RuntimeException("workorderItem not found with ID: " + itemId);
        }
    }
}