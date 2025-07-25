package com.cmms10.memo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Objects;

/**
 * cmms10 - MemoComment
 * 메모 댓글 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memoComment")
@IdClass(MemoCommentIdClass.class)
public class MemoComment {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "memoId", nullable = false)
    private String memoId;

    @Id
    @Column(name = "commentId", nullable = false)
    private String commentId;

    @Lob // For TEXT type
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "parentId")
    private String parentId;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "sortOrder")
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "companyId", referencedColumnName = "companyId", insertable = false, updatable = false),
            @JoinColumn(name = "memoId", referencedColumnName = "memoId", insertable = false, updatable = false)
    })
    private Memo memo;

}
