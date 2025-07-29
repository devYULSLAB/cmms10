package com.cmms10.memo.service;

import com.cmms10.memo.entity.Memo;
import com.cmms10.memo.entity.MemoComment;
import com.cmms10.memo.repository.MemoRepository;
import com.cmms10.memo.repository.MemoCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

/**
 * cmms10 - MemoService
 * 메모 서비스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Service
@Transactional
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemoCommentRepository memoCommentRepository;

    public MemoService(MemoRepository memoRepository, MemoCommentRepository memoCommentRepository) {
        this.memoRepository = memoRepository;
        this.memoCommentRepository = memoCommentRepository;
    }

    /**
     * 메모를 저장합니다.
     * 
     * @param memo     메모
     * @param username 사용자 ID
     * @return 저장된 메모
     */
    @Transactional
    public synchronized Memo saveMemo(Memo memo, String username) {
        LocalDateTime now = LocalDateTime.now();

        if (memo.getMemoId() == null || memo.getMemoId().isEmpty()) {

            // 신규 등록
            // 회사별 최대 메모ID 조회 후 +1 값을 새 메모ID로 설정
            String maxMemoId = memoRepository.findMaxMemoIdByCompanyId(memo.getCompanyId());
            int newMemoId = (maxMemoId == null) ? 1 : Integer.parseInt(maxMemoId) + 1;

            memo.setMemoId(String.valueOf(newMemoId));
            memo.setCreateBy(username);
            memo.setCreateDate(now);
            memo.setViewCount(0);

        } else {
            // 수정
            memo.setUpdateBy(username);
            memo.setUpdateDate(now);
        }

        return memoRepository.save(memo);
    }

    /**
     * 페이징 처리된 메모 목록을 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param pageable  페이징 정보
     * @return 페이징된 메모 목록
     */
    @Transactional(readOnly = true)
    public Page<Memo> getMemoByCompanyId(String companyId, Pageable pageable) {
        return memoRepository.findByCompanyId(companyId, pageable);
    }

    /**
     * 페이징 처리된 메모 목록을 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param memoName  메모 이름 (부분 일치)
     * @param pageable  페이징 정보
     * @return 페이징된 메모 목록
     */
    @Transactional(readOnly = true)
    public Page<Memo> getMemoByCompanyIdAndMemoName(String companyId, String memoName, Pageable pageable) {
        return memoRepository.findByCompanyIdAndMemoNameContaining(companyId, memoName, pageable);
    }

    /**
     * 페이징 처리된 메모 목록을 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param createBy  메모 생성자 (부분 일치)
     * @param pageable  페이징 정보
     * @return 페이징된 메모 목록
     */
    @Transactional(readOnly = true)
    public Page<Memo> getMemoByCompanyIdAndCreateBy(String companyId, String createBy, Pageable pageable) {
        return memoRepository.findByCompanyIdAndCreateBy(companyId, createBy, pageable);
    }

    /**
     * 메모를 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param memoId    메모 ID
     * @return 메모
     */
    @Transactional
    public Memo getMemoByCompanyIdAndMemoId(String companyId, String memoId) {
        Memo memo = memoRepository.findByCompanyIdAndMemoId(companyId, memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found: " + memoId));

        // 조회된 메모가 있으면 조회수 증가
        memo.setViewCount(memo.getViewCount() + 1);
        memoRepository.save(memo);

        return memo;
    }

    /**
     * 메모를 삭제합니다.
     * 
     * @param companyId 회사 ID
     * @param memoId    메모 ID
     * @param username  사용자 ID
     */
    @Transactional
    public void deleteMemo(String companyId, String memoId) {
        Memo memo = memoRepository.findByCompanyIdAndMemoId(companyId, memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found: " + memoId));

        // 1. First delete all related comments
        List<MemoComment> comments = memoCommentRepository
                .findByCompanyIdAndMemoIdOrderBySortOrderAsc(companyId, memoId);
        memoCommentRepository.deleteAll(comments);

        // 2. Then delete the memo
        memoRepository.delete(memo);
    }

    /**
     * 메모 댓글 목록을 조회합니다.
     * 
     * @param companyId 회사 ID
     * @param memoId    메모 ID
     * @return 댓글 목록
     */
    @Transactional(readOnly = true)
    public List<MemoComment> getMemoCommentList(String companyId, String memoId) {
        return memoCommentRepository.findByCompanyIdAndMemoIdOrderBySortOrderAsc(companyId, memoId);
    }

    /**
     * 메모 댓글을 저장합니다.
     * 
     * @param comment 댓글
     * @return 저장된 댓글
     */
    @Transactional
    public MemoComment saveMemoComment(MemoComment comment) {
        String maxCommentId = memoCommentRepository.findMaxCommentIdByCompanyIdAndMemoId(
                comment.getCompanyId(),
                comment.getMemoId());
        int newCommentId = (maxCommentId == null) ? 1 : Integer.parseInt(maxCommentId) + 1;
        comment.setCommentId(String.valueOf(newCommentId));
        comment.setSortOrder(newCommentId); // 댓글 ID를 정렬 순서로 사용
        return memoCommentRepository.save(comment);
    }

    /** 메모 댓글을 삭제합니다. */
    @Transactional
    public void deleteMemoComment(String companyId, String memoId, String commentId) {
        Optional<MemoComment> commentOpt = memoCommentRepository.findByCompanyIdAndMemoIdAndCommentId(
                companyId, memoId, commentId);
        if (commentOpt.isPresent()) {
            memoCommentRepository.delete(commentOpt.get());
        } else {
            throw new RuntimeException("Comment not found with ID: " + commentId);
        }
    }
}