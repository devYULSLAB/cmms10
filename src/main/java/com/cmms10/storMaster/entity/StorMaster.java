package com.cmms10.storMaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "storMaster")
@IdClass(StorMasterId.class)
public class StorMaster {
    @Id
    @Column(name = "companyId", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "siteId", length = 5, nullable = false)
    private String siteId;

    @Id
    @Column(name = "locId", length = 5, nullable = false)
    private String locId;

    @Column(name = "parentLocId", length = 5)
    private String parentLocId;

    @Column(name = "locType", length = 5)
    private String locType;

    @Column(name = "locName", length = 100)
    private String locName;
} 