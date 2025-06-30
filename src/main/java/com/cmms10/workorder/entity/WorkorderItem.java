package com.cmms10.workorder.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * cmms10 - workorderItem
 * 작업 오더 항목 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Entity
@Table(name = "workorderItem")
@IdClass(WorkorderItemIdClass.class)
@Getter
@Setter
@NoArgsConstructor
public class WorkorderItem {

    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "orderId", length = 10, nullable = false)
    private String orderId;

    @Id
    @Column(name = "itemId", length = 2, nullable = false)
    private String itemId;

    @Column(name = "itemName", length = 100)
    private String itemName;

    @Column(name = "itemMethod", length = 100)
    private String itemMethod;

    @Column(name = "itemResult", length = 100)
    private String itemResult;

    @Lob
    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "companyId", referencedColumnName = "companyId", insertable = false, updatable = false),
        @JoinColumn(name = "orderId", referencedColumnName = "orderId", insertable = false, updatable = false)
    })
    private Workorder workorder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkorderItem that = (WorkorderItem) o;
        return Objects.equals(companyId, that.companyId) &&
               Objects.equals(orderId, that.orderId) &&
               Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, orderId, itemId);
    }
}
