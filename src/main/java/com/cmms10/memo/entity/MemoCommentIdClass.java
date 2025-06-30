package com.cmms10.memo.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * cmms10 - MemoCommentIdClass
 * 메모 댓글 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
public class MemoCommentIdClass implements Serializable {
    private String companyId;
    private String memoId;
    private String commentId;

    // Constructors
    public MemoCommentIdClass() {
    }

    public MemoCommentIdClass(String companyId, String memoId, String commentId) {
        this.companyId = companyId;
        this.memoId = memoId;
        this.commentId = commentId;
    }

    // Getters and Setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMemoId() {
        return memoId;
    }

    public void setMemoId(String memoId) {
        this.memoId = memoId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoCommentIdClass that = (MemoCommentIdClass) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(memoId, that.memoId) &&
               Objects.equals(commentId, that.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, memoId, commentId);
    }
}
