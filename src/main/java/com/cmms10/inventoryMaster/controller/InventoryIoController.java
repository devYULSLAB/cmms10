package com.cmms10.inventoryMaster.controller;

import com.cmms10.inventoryMaster.service.InventoryIoService;
import com.cmms10.inventoryMaster.entity.InventoryHistory;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.CannotAcquireLockException;

import java.util.List;

/**
 * cmms10 - InventoryIoController
 * 재고 입출고 처리 컨트롤러 (히스토리 저장 포함)
 * 화면에서 Handsontable 대량 입력 처리 대응
 * 
 * @author cmms10
 * @since 2024-06-25
 */

@RestController
@RequestMapping("/inventoryIo")
@RequiredArgsConstructor
public class InventoryIoController {

    private final InventoryIoService inventoryIoService;

    /**
     * 재고 입출고 저장
     * @param ioList 입출고 내역 리스트 (InventoryHistory 엔티티 구조 그대로 전달)
     * @param session 세션
     * @return 결과 문자열
     */
    @PostMapping("/InventoryIoSave")
    public String saveInventoryIo(@RequestBody List<InventoryHistory> ioList, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");

        try {
            ioList.forEach(io -> io.setCompanyId(companyId));
            inventoryIoService.processInventoryIo(ioList, username);
            return "success";
        } catch (CannotAcquireLockException e) {
            return "lock: 다른 사용자가 처리 중입니다. 잠시만 기다려주세요.";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/history/{inventoryId}")
    public List<InventoryHistory> getHistory(@PathVariable String inventoryId, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        return inventoryIoService.getInventoryHistory(companyId, inventoryId);
    }
}
