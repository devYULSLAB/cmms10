package com.cmms10.domain.site.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * cmms10 - Site
 * 사이트 정보 관리 엔티티
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Getter
@Setter
@Entity
@Table(name = "site")
@IdClass(SiteIdClass.class)
public class Site {

    @Id
    @Column(name = "companyId", length = 5)
    private String companyId;

    @Id
    @Column(name = "siteId", length = 5)
    private String siteId;

    @Column(name = "siteName", length = 100)
    private String siteName;

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

    @Column(name = "deleteMark", length = 1)
    private String deleteMark;

    // Constructors
    public Site() {
    }

    public Site(String companyId, String siteId) {
        this.companyId = companyId;
        this.siteId = siteId;
    }

    public Site(String companyId, String siteId, String siteName) {
        this.companyId = companyId;
        this.siteId = siteId;
        this.siteName = siteName;
    }
}
