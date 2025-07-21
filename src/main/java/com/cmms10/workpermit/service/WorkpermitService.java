package com.cmms10.workpermit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmms10.workpermit.entity.Workpermit;
import com.cmms10.workpermit.entity.WorkpermitItem;
import com.cmms10.workpermit.repository.WorkpermitRepository;
import com.cmms10.workpermit.repository.WorkpermitItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class WorkpermitService {

    private final WorkpermitRepository workpermitRepository;
    private final WorkpermitItemRepository workpermitItemRepository;

    public WorkpermitService(WorkpermitRepository workpermitRepository, WorkpermitItemRepository workpermitItemRepository) {
        this.workpermitRepository = workpermitRepository;
        this.workpermitItemRepository = workpermitItemRepository;
    }

    public Page<Workpermit> getAllWorkpermits(String companyId, String siteId, Pageable pageable) {
        return workpermitRepository.findByCompanyIdAndSiteId(companyId, siteId, pageable);
    }

    public Workpermit getWorkpermitByCompanyIdAndPermitId(String companyId, String siteId, String permitId) {
        return workpermitRepository.findByCompanyIdAndPermitId(companyId, permitId)
                .orElseThrow(() -> new IllegalArgumentException("작업허가를 찾을 수 없습니다: " + permitId));
    }

    public List<WorkpermitItem> getWorkpermitItems(String companyId, String permitId) {
        return workpermitItemRepository.findByCompanyIdAndPermitId(companyId, permitId);
    }

    public Workpermit saveWorkpermit(Workpermit workpermit, String username) {
        LocalDateTime now = LocalDateTime.now();
        boolean isNewWorkpermit = (workpermit.getPermitId() == null || workpermit.getPermitId().isEmpty());
        if (isNewWorkpermit) {
            String maxPermitId = workpermitRepository.findMaxPermitIdByCompanyId(workpermit.getCompanyId());
            String newPermitId = (maxPermitId == null) ? "9000000000" : String.valueOf(Long.parseLong(maxPermitId) + 1);
            workpermit.setPermitId(newPermitId);
            workpermit.setCreateDate(now);
            workpermit.setCreateBy(username);
        } else {
            workpermit.setUpdateDate(now);
            workpermit.setUpdateBy(username);
        }
        // WorkpermitItem 처리
        List<WorkpermitItem> items = workpermit.getItems();
        if (items != null && !items.isEmpty()) {
            // 새로운 리스트를 생성하여 유효한 항목만 추가
            List<WorkpermitItem> validItems = new ArrayList<>();
            int itemIndex = 1;

            for (WorkpermitItem item : items) {
                if (item.getSignerName() != null && !item.getSignerName().isEmpty()) {
                    item.setCompanyId(workpermit.getCompanyId());
                    item.setPermitId(workpermit.getPermitId());
                    item.setItemId(String.format("%02d", itemIndex++));
                    item.setWorkpermit(workpermit);
                    validItems.add(item);
                }
            }
            
            // 기존 리스트를 비우고 유효한 항목들로 교체
            items.clear();
            items.addAll(validItems);
        }

        return workpermitRepository.save(workpermit);
    }

    public WorkpermitItem saveWorkpermitItem(WorkpermitItem workpermitItem, String username) {
        String maxItemId = workpermitItemRepository.findMaxItemIdByCompanyIdAndPermitId(
            workpermitItem.getCompanyId(),
            workpermitItem.getPermitId()
        );
        int newItemId = (maxItemId == null) ? 1 : Integer.parseInt(maxItemId) + 1;
        workpermitItem.setItemId(String.valueOf(newItemId));

        return workpermitItemRepository.save(workpermitItem);
    }

    public void deleteWorkpermit(String companyId, String permitId) {
        Optional<Workpermit> workpermitOpt = workpermitRepository.findByCompanyIdAndPermitId(
            companyId, 
            permitId
        );
        
        if (workpermitOpt.isPresent()) {
            Workpermit workpermit = workpermitOpt.get();
            // Delete associated items first
            List<WorkpermitItem> items = workpermitItemRepository.findByCompanyIdAndPermitId(companyId, permitId);
            workpermitItemRepository.deleteAll(items);
            // Delete the work order
            workpermitRepository.delete(workpermit);
        } else {
            throw new RuntimeException("workpermit not found with ID: " + permitId);
        }
    }

    public void deleteWorkpermitItem(String companyId, String permitId, String itemId) {
        Optional<WorkpermitItem> itemOpt = workpermitItemRepository.findByCompanyIdAndPermitIdAndItemIdOrderByItemIdAsc(
            companyId, 
            permitId, 
            itemId
        );
        
        if (itemOpt.isPresent()) {
            WorkpermitItem item = itemOpt.get();
            workpermitItemRepository.delete(item);
        } else {
            throw new RuntimeException("workpermitItem not found with ID: " + itemId);
        }
    }



}
