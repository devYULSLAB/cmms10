package com.cmms10.domain.site.entity;

import java.io.Serializable;

/**
 * cmms10 - SiteIdClass
 * 사이트 복합키 클래스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
public class SiteIdClass implements Serializable {
    private String companyId;
    private String siteId;

    // Default constructor
    public SiteIdClass() {
    }

    // Constructor with fields
    public SiteIdClass(String companyId, String siteId) {
        this.companyId = companyId;
        this.siteId = siteId;
    }

    // Getters and setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    // hashCode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteIdClass that = (SiteIdClass) o;
        return companyId.equals(that.companyId) && siteId.equals(that.siteId);
    }

    @Override
    public int hashCode() {
        return 31 * companyId.hashCode() + siteId.hashCode();
    }
}
