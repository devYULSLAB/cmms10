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
public class WorkorderService {
    private final WorkorderRepository workorderRepository;
    private final WorkorderItemRepository workorderItemRepository;

    public WorkorderService(
            WorkorderRepository workorderRepository,
            WorkorderItemRepository workorderItemRepository) {
        this.workorderRepository = workorderRepository;
        this.workorderItemRepository = workorderItemRepository;
    }

    @Transactional(readOnly = true)
    public Page<Workorder> getAllWorkorders(String companyId, String siteId, Pageable pageable) {
        return workorderRepository.findByCompanyIdAndSiteId(companyId, siteId, pageable);
    }

    @Transactional(readOnly = true)
    public Workorder getWorkorderByWorkorderId(String companyId, String orderId) {
        return workorderRepository.findByCompanyIdAndOrderId(companyId, orderId)
                .orElseThrow(() -> new IllegalArgumentException("작업지시를 찾을 수 없습니다: " + orderId));
    }

    @Transactional(readOnly = true)
    public List<WorkorderItem> getWorkorderItems(String companyId, String orderId) {
        return workorderItemRepository.findByCompanyIdAndOrderIdOrderByItemIdAsc(companyId, orderId);
    }

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

    @Transactional
    public WorkorderItem saveWorkorderItem(WorkorderItem workorderItem, String username) {

        String maxItemId = workorderItemRepository.findMaxItemIdByCompanyIdAndOrderId(
            workorderItem.getCompanyId(),
            workorderItem.getOrderId()
        );
        int newItemId = (maxItemId == null) ? 1 : Integer.parseInt(maxItemId) + 1;
        workorderItem.setItemId(String.valueOf(newItemId));

        return workorderItemRepository.save(workorderItem);
    }

    @Transactional
    public void deleteWorkorder(String companyId, String workorderId) {
        Optional<Workorder> workorderOpt = workorderRepository.findByCompanyIdAndOrderId(
            companyId, 
            workorderId
        );
        
        if (workorderOpt.isPresent()) {
            Workorder workorder = workorderOpt.get();
            // Delete associated items first
            List<WorkorderItem> items = workorderItemRepository.findByCompanyIdAndOrderIdOrderByItemIdAsc(companyId, workorderId);
            workorderItemRepository.deleteAll(items);
            // Delete the work order
            workorderRepository.delete(workorder);
        } else {
            throw new RuntimeException("workorder not found with ID: " + workorderId);
        }
    }

    @Transactional
    public void deleteWorkorderItem(String companyId, String workorderId, String itemId) {
        Optional<WorkorderItem> itemOpt = workorderItemRepository.findByCompanyIdAndOrderIdAndItemId(
            companyId, 
            workorderId, 
            itemId
        );
        
        if (itemOpt.isPresent()) {
            WorkorderItem item = itemOpt.get();
            workorderItemRepository.delete(item);
        } else {
            throw new RuntimeException("workorderItem not found with ID: " + itemId);
        }
    }
}