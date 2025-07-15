package com.cmms10.memo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * cmms10 - Memo
 * 메모 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memo")
@IdClass(MemoIdClass.class)
public class Memo {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "memoId", nullable = false)
    private String memoId;

    @Column(name = "memoName", length = 100)
    private String memoName;

    @Lob // For TEXT type
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "isPinned", length = 1)
    private String isPinned;

    @Column(name = "viewCount")
    private Integer viewCount;

    @Column(name = "siteId", length = 5)
    private String siteId;

    @Column(name = "fileGroupId", length = 10)
    private String fileGroupId;

    @Column(name = "createBy", length = 5)
    private String createBy;

    @Column(name = "createDate")
    private LocalDateTime createDate;

    @Column(name = "updateBy", length = 5)
    private String updateBy;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    // Constructors
    // public Memo() {
    // }

    // public Memo(String companyId, String memoId) {
    //     this.companyId = companyId;
    //     this.memoId = memoId;
    // }

    // public Memo(String companyId, String memoId, String memoName) {
    //     this.companyId = companyId;
    //     this.memoId = memoId;
    //     this.memoName = memoName;
    // }

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;
    //     Memo memo = (Memo) o;
    //     return Objects.equals(companyId, memo.companyId) &&
    //            Objects.equals(memoId, memo.memoId);
    // }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(companyId, memoId);
    // }
}
