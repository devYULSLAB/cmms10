package com.cmms10.memo.controller;

import com.cmms10.memo.entity.Memo;
import com.cmms10.memo.entity.MemoComment;
import com.cmms10.memo.service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

/**
 * cmms10 - MemoController
 * 메모 컨트롤러
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Controller
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    /**
     * 메모 등록 화면
     * 
     * @return 메모 등록 화면
     */
    @GetMapping("/memoForm")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Memo memo = new Memo();
        memo.setCompanyId(companyId);
        model.addAttribute("memo", memo);

        return "memo/memoForm";
    }

    /**
     * 메모 수정 화면 (memoId로 조회)
     */
    @GetMapping("/memoForm/{memoId}")
    public String editForm(@PathVariable String memoId,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String companyId = (String) session.getAttribute("companyId");
        if (companyId == null) {
            return "redirect:/login";
        }

        Memo memo = memoService.getMemoByCompanyIdAndMemoId(companyId, memoId);
        List<MemoComment> commentList = memoService.getMemoCommentList(companyId, memoId);
        memo.setCommentList(commentList);

        model.addAttribute("memo", memo);

        return "memo/memoForm";
    }

    /**
     * 메모를 저장합니다.
     * 
     * @param memo    메모
     * @param session 세션
     * @return 메모 목록 화면으로 리다이렉트
     */
    @PostMapping("/memoSave")
    public String save(@ModelAttribute Memo memo, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");
        String username = (String) session.getAttribute("username");
        // 메모 객체에 정보 설정
        memo.setCompanyId(companyId);
        memo.setCreateBy(username);

        // 세션 정보가 없는 경우 처리
        if (companyId == null || username == null) {
            // 로깅 또는 에러 처리
            return "redirect:/login";
        }
        // 메모 저장
        memoService.saveMemo(memo, username);
        return "redirect:/memo/memoList";
    }

    /**
     * 메모 목록 화면을 조회합니다.
     * 
     * @param model   모델
     * @param session 세션
     * @param page    페이지 번호
     * @param size    페이지 크기
     * @return 메모 목록 화면
     */
    @GetMapping("/memoList")
    public String list(Model model, HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Order.desc("isPinned"), Sort.Order.desc("createDate")));

        // Page 객체로 메모 목록 조회
        Page<Memo> memos = memoService.getMemoByCompanyId(companyId, pageable);
        model.addAttribute("memos", memos);

        return "memo/memoList";
    }

    /**
     * 메모 상세 화면을 조회합니다.
     * 
     * @param model   모델
     * @param session 세션
     * @param memoId  메모 ID
     * @return 메모 상세 화면
     */
    @GetMapping("/memoDetail/{memoId}")
    public String detail(Model model,
            HttpSession session,
            @PathVariable String memoId,
            RedirectAttributes redirectAttributes) {
        // 1. Session validation first
        String companyId = (String) session.getAttribute("companyId");
        if (companyId == null) {
            return "redirect:/login";
        }

        try {
            // 2. Get memo first
            Memo memo = memoService.getMemoByCompanyIdAndMemoId(companyId, memoId);

            // 3. Get comments only if memo exists
            List<MemoComment> commentList = memoService.getMemoCommentList(companyId, memoId);

            // 4. Add attributes to model
            model.addAttribute("memo", memo);
            model.addAttribute("commentList", commentList != null ? commentList : Collections.emptyList());

            return "memo/memoDetail";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "메모 조회 중 오류가 발생했습니다.");
            return "redirect:/memo/memoList";
        }
    }

    /**
     * 메모를 삭제합니다.
     * 
     * @param memoId  메모 ID
     * @param session 세션
     * @return 메모 목록 화면으로 리다이렉트
     */
    @PostMapping("/memoDelete/{memoId}")
    public String delete(@PathVariable String memoId, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        memoService.deleteMemo(companyId, memoId);
        return "redirect:/memo/memoList";
    }

    /**
     * 메모 댓글을 저장합니다.
     * 
     * @param comment 댓글
     * @param session 세션
     * @return 메모 상세 화면으로 리다이렉트
     */
    @PostMapping("/memoComment/save")
    public String saveComment(@ModelAttribute MemoComment comment,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            // Get session info
            String companyId = (String) session.getAttribute("companyId");
            comment.setCompanyId(companyId);

            System.out.println("companyId: " + comment.getCompanyId());
            System.out.println("memoId: " + comment.getMemoId());
            System.out.println("note: " + comment.getNote());

            MemoComment savedComment = memoService.saveMemoComment(comment);
            redirectAttributes.addFlashAttribute("successMessage", "Comment saved successfully");

            return "redirect:/memo/memoDetail/" + savedComment.getMemoId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save comment: " + e.getMessage());
            return "redirect:/memo/memoDetail/" + comment.getMemoId();
        }
    }

    /**
     * 메모 댓글을 삭제합니다.
     * 
     * @param commentId 댓글 ID
     * @param memoId    메모 ID
     * @param session   세션
     * @return 메모 상세 화면으로 리다이렉트
     */
    @PostMapping("/memoComment/delete")
    public String deleteComment(@RequestParam String commentId,
            @RequestParam String memoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            // Get session info
            String companyId = (String) session.getAttribute("companyId");
            memoService.deleteMemoComment(companyId, memoId, commentId);
            redirectAttributes.addFlashAttribute("successMessage", "Comment deleted successfully");
            return "redirect:/memo/memoDetail/" + memoId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete comment: " + e.getMessage());
            return "redirect:/memo/memoDetail/" + memoId;
        }
    }

}
