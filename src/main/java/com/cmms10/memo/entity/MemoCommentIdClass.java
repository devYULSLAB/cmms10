package com.cmms10.memo.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * cmms10 - MemoCommentIdClass
 * 메모 댓글 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoCommentIdClass implements Serializable {
    private String companyId;
    private String memoId;
    private String commentId;
}
