package com.cmms10.checksheet.controller;

import com.cmms10.checksheet.entity.ChecksheetResult;
import com.cmms10.checksheet.service.ChecksheetResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

/**
 * 패키지: com.cmms10.checksheet.controller
 * 클래스: ChecksheetResultController
 * 주요기능: 체크시트 결과 관련 컨트롤러
 * 생성자: (로그인 이름 입력)
 * 생성일: (생성일 입력)
 */
@Controller
@RequestMapping("/checksheet")
public class ChecksheetResultController {

    private final ChecksheetResultService checksheetResultService;

    public ChecksheetResultController(ChecksheetResultService checksheetResultService) {
        this.checksheetResultService = checksheetResultService;
    }

    /**
     * 체크시트 결과 상세 페이지를 표시합니다.
     * 
     * @param permitId 허가서 ID
     * @param model    모델 객체
     * @param session  세션 객체
     * @return 체크시트 결과 상세 페이지
     */
    @GetMapping("/checksheetResultDetail/{permitId}")
    public String checksheetResultDetail(@PathVariable String permitId,
            Model model,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        try {
            ChecksheetResult result = checksheetResultService.getResultByCompanyIdAndPermitId(companyId, permitId);
            model.addAttribute("checksheetResult", result);

            // 결과 요약 정보 생성
            Map<String, Object> summary = createSummary(result);
            model.addAttribute("summary", summary);

            return "workpermit/checksheetResultDetail";
        } catch (RuntimeException e) {
            // 결과가 없는 경우 빈 모델로 페이지 표시
            model.addAttribute("checksheetResult", null);
            model.addAttribute("summary", null);
            return "workpermit/checksheetResultDetail";
        }
    }

    /**
     * 체크시트 결과에서 요약 정보를 생성합니다.
     * 
     * @param result 체크시트 결과
     * @return 요약 정보 맵
     */
    private Map<String, Object> createSummary(ChecksheetResult result) {
        Map<String, Object> summary = new HashMap<>();

        try {
            if (result.getCheckResultJson() != null && !result.getCheckResultJson().isEmpty()) {
                // JSON 파싱하여 항목 수 계산 (간단한 구현)
                String json = result.getCheckResultJson();
                int totalItems = countJsonFields(json);
                int completedItems = countCompletedFields(json);

                summary.put("totalItems", totalItems);
                summary.put("completedItems", completedItems);
                summary.put("completionRate",
                        totalItems > 0 ? Math.round((double) completedItems / totalItems * 100) : 0);
            } else {
                summary.put("totalItems", 0);
                summary.put("completedItems", 0);
                summary.put("completionRate", 0);
            }
        } catch (Exception e) {
            summary.put("totalItems", 0);
            summary.put("completedItems", 0);
            summary.put("completionRate", 0);
        }

        return summary;
    }

    /**
     * JSON 문자열에서 필드 수를 계산합니다.
     * 
     * @param json JSON 문자열
     * @return 필드 수
     */
    private int countJsonFields(String json) {
        // 간단한 구현 - 실제로는 JSON 파서 사용 권장
        return json.split("\"").length / 2;
    }

    /**
     * JSON 문자열에서 완료된 필드 수를 계산합니다.
     * 
     * @param json JSON 문자열
     * @return 완료된 필드 수
     */
    private int countCompletedFields(String json) {
        // 간단한 구현 - 실제로는 JSON 파서 사용 권장
        return json.split("true").length - 1;
    }
}