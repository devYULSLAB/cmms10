package com.cmms10.workpermit.service;

import com.cmms10.workpermit.entity.Workpermit;
import com.cmms10.workpermit.entity.WorkpermitItem;
import com.cmms10.workpermit.repository.WorkpermitRepository;
import com.cmms10.workpermit.repository.WorkpermitItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 패키지: com.cmms10.workpermit.service
 * 클래스: WorkpermitService
 * 주요기능: 작업허가서 관리 서비스
 * 생성자: (로그인 이름 입력)
 * 생성일: (생성일 입력)
 */
@Service
@Transactional
public class WorkpermitService {

    private final WorkpermitRepository workpermitRepository;
    private final WorkpermitItemRepository workpermitItemRepository;

    /**
     * WorkpermitService 생성자
     * 
     * @param workpermitRepository     작업허가서 레포지토리
     * @param workpermitItemRepository 작업허가서 항목 레포지토리
     */
    public WorkpermitService(WorkpermitRepository workpermitRepository,
            WorkpermitItemRepository workpermitItemRepository) {
        this.workpermitRepository = workpermitRepository;
        this.workpermitItemRepository = workpermitItemRepository;
    }

    /**
     * 회사 ID로 작업허가서 목록을 페이징하여 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param pageable  페이징 정보
     * @return 작업허가서 페이지
     */
    @Transactional(readOnly = true)
    public Page<Workpermit> getAllWorkpermitsByCompanyId(String companyId, Pageable pageable) {
        return workpermitRepository.findByCompanyIdOrderByPermitIdAsc(companyId, pageable);
    }

    /**
     * 회사 ID와 허가서 ID로 작업허가서를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param siteId    사이트 ID (사용하지 않음, 호환성을 위해 유지)
     * @param permitId  허가서 ID
     * @return 작업허가서 정보
     * @throws IllegalArgumentException 허가서를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Workpermit getWorkpermitByCompanyIdAndPermitId(String companyId, String siteId, String permitId) {
        return workpermitRepository.findByCompanyIdAndPermitId(companyId, permitId)
                .orElseThrow(() -> new IllegalArgumentException("Workpermit not found with ID: " + permitId));
    }

    /**
     * 회사 ID와 허가서 ID로 작업허가서 항목 목록을 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param permitId  허가서 ID
     * @return 작업허가서 항목 목록
     */
    @Transactional(readOnly = true)
    public List<WorkpermitItem> getWorkpermitItems(String companyId, String permitId) {
        return workpermitItemRepository.findByCompanyIdAndPermitId(companyId, permitId);
    }

    /**
     * 작업허가서를 저장합니다 (신규 등록 또는 수정).
     * 
     * @param workpermit 저장할 작업허가서 정보
     * @param username   사용자 이름 (등록자 또는 수정자)
     * @return 저장된 작업허가서 정보
     */
    public Workpermit saveWorkpermit(Workpermit workpermit, String username) {
        LocalDateTime now = LocalDateTime.now();
        boolean isNewWorkpermit = (workpermit.getPermitId() == null || workpermit.getPermitId().isEmpty());

        if (isNewWorkpermit) {
            // 신규 등록: 새로운 허가서 ID 생성
            String maxPermitId = workpermitRepository.findMaxPermitIdByCompanyId(workpermit.getCompanyId());
            String newPermitId = (maxPermitId == null) ? "9000000000" : String.valueOf(Long.parseLong(maxPermitId) + 1);
            workpermit.setPermitId(newPermitId);
            workpermit.setCreateDate(now);
            workpermit.setCreateBy(username);
        } else {
            // 수정: 수정 정보 설정
            workpermit.setUpdateDate(now);
            workpermit.setUpdateBy(username);
        }

        // WorkpermitItem 처리
        processWorkpermitItems(workpermit);

        // Workpermit 저장
        Workpermit savedWorkpermit = workpermitRepository.save(workpermit);

        // WorkpermitItem들을 개별적으로 저장
        if (workpermit.getItems() != null && !workpermit.getItems().isEmpty()) {
            for (WorkpermitItem item : workpermit.getItems()) {
                try {
                    // 명시적으로 모든 필드 설정
                    item.setCompanyId(workpermit.getCompanyId());
                    item.setPermitId(workpermit.getPermitId());

                    // null 값 처리
                    if (item.getSignature() == null) {
                        item.setSignature("");
                    }
                    if (item.getSignerName() == null) {
                        item.setSignerName("");
                    }
                    if (item.getRole() == null) {
                        item.setRole("");
                    }
                    if (item.getNote() == null) {
                        item.setNote("");
                    }

                    workpermitItemRepository.save(item);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        return savedWorkpermit;
    }

    /**
     * 작업허가서 항목을 저장합니다.
     * 
     * @param workpermitItem 저장할 작업허가서 항목
     * @param username       사용자 이름
     * @return 저장된 작업허가서 항목
     */
    public WorkpermitItem saveWorkpermitItem(WorkpermitItem workpermitItem, String username) {
        String maxItemId = workpermitItemRepository.findMaxItemIdByCompanyIdAndPermitId(
                workpermitItem.getCompanyId(),
                workpermitItem.getPermitId());
        int newItemId = (maxItemId == null) ? 1 : Integer.parseInt(maxItemId) + 1;
        workpermitItem.setItemId(String.valueOf(newItemId));

        return workpermitItemRepository.save(workpermitItem);
    }

    /**
     * 작업허가서를 삭제합니다.
     * 
     * @param companyId 회사 ID
     * @param permitId  허가서 ID
     * @throws RuntimeException 허가서를 찾을 수 없는 경우
     */
    public void deleteWorkpermit(String companyId, String permitId) {
        Optional<Workpermit> workpermitOpt = workpermitRepository.findByCompanyIdAndPermitId(companyId, permitId);

        if (workpermitOpt.isPresent()) {
            Workpermit workpermit = workpermitOpt.get();
            // 연관된 항목들을 먼저 삭제
            List<WorkpermitItem> items = workpermitItemRepository.findByCompanyIdAndPermitId(companyId, permitId);
            workpermitItemRepository.deleteAll(items);
            // 작업허가서 삭제
            workpermitRepository.delete(workpermit);
        } else {
            throw new RuntimeException("workpermit not found with ID: " + permitId);
        }
    }

    /**
     * 작업허가서 항목을 삭제합니다.
     * 
     * @param companyId 회사 ID
     * @param permitId  허가서 ID
     * @param itemId    항목 ID
     * @throws RuntimeException 항목을 찾을 수 없는 경우
     */
    public void deleteWorkpermitItem(String companyId, String permitId, String itemId) {
        Optional<WorkpermitItem> itemOpt = workpermitItemRepository.findByCompanyIdAndPermitIdAndItemIdOrderByItemIdAsc(
                companyId, permitId, itemId);

        if (itemOpt.isPresent()) {
            WorkpermitItem item = itemOpt.get();
            workpermitItemRepository.delete(item);
        } else {
            throw new RuntimeException("workpermitItem not found with ID: " + itemId);
        }
    }

    /**
     * 작업허가서 항목들을 처리합니다.
     * 유효한 항목만 필터링하고 ID를 설정합니다.
     * 
     * @param workpermit 처리할 작업허가서
     */
    private void processWorkpermitItems(Workpermit workpermit) {
        List<WorkpermitItem> items = workpermit.getItems();
        if (items != null && !items.isEmpty()) {
            // 새로운 리스트를 생성하여 유효한 항목만 추가
            List<WorkpermitItem> validItems = new ArrayList<>();
            int itemIndex = 1;

            for (WorkpermitItem item : items) {
                if (item.getSignerName() != null && !item.getSignerName().isEmpty()) {
                    item.setCompanyId(workpermit.getCompanyId());
                    item.setPermitId(workpermit.getPermitId());
                    item.setItemId(String.format("%02d", itemIndex));
                    item.setWorkpermit(workpermit);

                    // 서명 데이터가 있으면 signedAt 설정
                    if (item.getSignature() != null && !item.getSignature().trim().isEmpty()) {
                        item.setSignedAt(LocalDateTime.now());
                    }

                    validItems.add(item);
                    itemIndex++;
                }
            }

            // 기존 리스트를 비우고 유효한 항목들로 교체
            items.clear();
            items.addAll(validItems);
        }
    }
}
