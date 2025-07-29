package com.cmms10.inspection.service;

import com.cmms10.inspection.entity.Inspection;
import com.cmms10.inspection.entity.InspectionItem;

import com.cmms10.inspection.repository.InspectionRepository;
import com.cmms10.inspection.repository.InspectionItemRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * cmms10 - InspectionService
 * 점검 관리 서비스
 * 
 * @author cmms10
 * @since 2025-05-29
 */
@Service
@Transactional
public class InspectionService {

    private final InspectionRepository inspectionRepository;
    private final InspectionItemRepository inspectionItemRepository;

    public InspectionService(
            InspectionRepository inspectionRepository,
            InspectionItemRepository inspectionItemRepository) {
        this.inspectionRepository = inspectionRepository;
        this.inspectionItemRepository = inspectionItemRepository;
    }

    /**
     * 모든 점검 목록을 페이지네이션으로 조회
     * 
     * @param companyId 회사 ID
     * @param pageable  페이지 정보
     * @return 점검 목록 페이지
     */
    @Transactional(readOnly = true)
    public Page<Inspection> getAllInspectionsByCompanyId(String companyId, Pageable pageable) {
        return inspectionRepository.findByCompanyIdOrderByInspectionIdAsc(companyId, pageable);
    }

    /**
     * 모든 점검 목록을 페이지네이션으로 조회
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @param pageable  페이지 정보
     * @return 점검 목록 페이지
     */
    @Transactional(readOnly = true)
    public Page<Inspection> getAllInspectionsByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable) {
        return inspectionRepository.findByCompanyIdAndSiteIdOrderByInspectionIdAsc(companyId, siteId, pageable);
    }

    /**
     * 점검 ID로 점검 정보 조회
     * 
     * @param companyId    회사 ID
     * @param siteId       사이트 ID
     * @param inspectionId 점검 ID
     * @return 점검 정보 Optional
     */
    @Transactional(readOnly = true)
    public Inspection getInspectionByCompanyIdAndSiteIdAndInspectionId(String companyId, String siteId,
            String inspectionId) {
        return inspectionRepository.findByCompanyIdAndSiteIdAndInspectionId(companyId, siteId, inspectionId)
                .orElseThrow(() -> new RuntimeException("해당 점검이 존재하지 않습니다."));
    }

    /**
     * plantId로 점검 정보 조회
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID
     * @param plantId   설비 ID
     * @return 점검 정보 Optional
     */
    @Transactional(readOnly = true)
    public Page<Inspection> getInspectionByCompanyIdAndSiteIdAndPlantId(String companyId, String siteId, String plantId,
            Pageable pageable) {
        return inspectionRepository.findByCompanyIdAndSiteIdAndPlantIdOrderByInspectionIdAsc(companyId, siteId, plantId,
                pageable);
    }

    /**
     * 점검 정보 저장 (신규 등록 또는 수정)
     * 
     * @param inspection 저장할 점검 정보
     * @param username   사용자 이름 (등록자 또는 수정자)
     * @return 저장된 점검 정보
     */
    @Transactional
    public Inspection saveInspection(Inspection inspection, String username) {
        LocalDateTime now = LocalDateTime.now();
        boolean isNewInspection = (inspection.getInspectionId() == null || inspection.getInspectionId().isEmpty());

        if (isNewInspection) {
            String maxInspectionId = inspectionRepository.findMaxInspectionIdByCompanyId(inspection.getCompanyId());
            String newInspectionId = (maxInspectionId == null) ? "3000000000"
                    : String.valueOf(Long.parseLong(maxInspectionId) + 1);

            inspection.setInspectionId(newInspectionId);
            inspection.setCreateDate(now);
            inspection.setCreateBy(username);
        } else {
            inspection.setUpdateDate(now);
            inspection.setUpdateBy(username);
        }

        // InspectionItem 처리
        List<InspectionItem> items = inspection.getItems();
        if (items != null && !items.isEmpty()) {
            // 새로운 리스트를 생성하여 유효한 항목만 추가
            List<InspectionItem> validItems = new ArrayList<>();
            int itemIndex = 1;

            for (InspectionItem item : items) {
                if (item.getItemName() != null && !item.getItemName().isEmpty()) {
                    item.setCompanyId(inspection.getCompanyId());
                    item.setInspectionId(inspection.getInspectionId());
                    item.setItemId(String.format("%02d", itemIndex++));
                    item.setInspection(inspection);
                    validItems.add(item);
                }
            }

            // 기존 리스트를 비우고 유효한 항목들로 교체
            items.clear();
            items.addAll(validItems);
        }

        return inspectionRepository.save(inspection);
    }

    /**
     * 점검 정보 삭제 (소프트 삭제)
     * 
     * @param companyId    회사 ID
     * @param inspectionId 점검 ID
     */
    @Transactional
    public void deleteInspection(String companyId, String siteId, String inspectionId) {
        Optional<Inspection> inspectionOpt = inspectionRepository.findByCompanyIdAndSiteIdAndInspectionId(companyId,
                siteId, inspectionId);

        if (inspectionOpt.isPresent()) {
            // 1. Delete all related inspection items first
            inspectionItemRepository.deleteByCompanyIdAndInspectionId(companyId, inspectionId);
            // 2. Finally delete the inspection
            inspectionRepository.deleteByCompanyIdAndSiteIdAndInspectionId(companyId, siteId, inspectionId);
        } else {
            throw new RuntimeException("Inspection not found with ID: " + inspectionId);
        }
    }

    /**
     * 점검 항목 목록 조회
     * 
     * @param companyId    회사 ID
     * @param siteId       사이트 ID
     * @param inspectionId 점검 ID
     * @return 점검 항목 목록
     */
    @Transactional(readOnly = true)
    public List<InspectionItem> getInspectionItemByCompanyIdAndInspectionId(String companyId, String inspectionId) {
        return inspectionItemRepository.findByCompanyIdAndInspectionId(companyId, inspectionId);
    }

}
