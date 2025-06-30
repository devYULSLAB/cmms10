package com.cmms10.commonCode.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

/**
 * cmms10 - CommonCodeItem
 * 공통 코드 아이템 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Getter
@Setter
@Entity
@Table(name = "commonCodeItem")
@IdClass(CommonCodeItemIdClass.class)
public class CommonCodeItem {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "codeId", length = 5, nullable = false)
    private String codeId;

    @Id
    @Column(name = "codeItemId", length = 5, nullable = false)
    private String codeItemId;

    @Column(name = "codeItemName", length = 100)
    private String codeItemName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "companyId", referencedColumnName = "companyId", insertable = false, updatable = false),
        @JoinColumn(name = "codeId", referencedColumnName = "codeId", insertable = false, updatable = false)
    })
    private CommonCode commonCode;

    // Constructors
    public CommonCodeItem() {
    }

    // equals and hashCode (only for PK fields)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonCodeItem that = (CommonCodeItem) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(codeId, that.codeId) &&
               Objects.equals(codeItemId, that.codeItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, codeId, codeItemId);
    }
}
