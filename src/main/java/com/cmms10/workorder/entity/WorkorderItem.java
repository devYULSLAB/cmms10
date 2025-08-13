package com.cmms10.workorder.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * cmms10 - workorderItem
 * 작업 오더 항목 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workorderItem")
@IdClass(WorkorderItemIdClass.class)
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
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "companyId", referencedColumnName = "companyId", insertable = false, updatable = false),
            @JoinColumn(name = "orderId", referencedColumnName = "orderId", insertable = false, updatable = false)
    })
    private Workorder workorder;
}
