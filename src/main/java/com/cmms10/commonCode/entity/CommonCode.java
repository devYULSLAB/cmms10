package com.cmms10.commonCode.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Objects;

/**
 * cmms10 - CommonCode
 * 공통 코드 마스터 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Getter
@Setter
@Entity
@Table(name = "commonCode")
@IdClass(CommonCodeIdClass.class)
public class CommonCode {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "codeId", length = 5, nullable = false)
    private String codeId;

    @Column(name = "codeType", length = 5)
    private String codeType;

    @Column(name = "codeName", length = 100)
    private String codeName;

    @OneToMany(mappedBy = "commonCode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommonCodeItem> items;

    // Constructors
    public CommonCode() {
    }

    // equals and hashCode (only for PK fields)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonCode that = (CommonCode) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(codeId, that.codeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, codeId);
    }
}
