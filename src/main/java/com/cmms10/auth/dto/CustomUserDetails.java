package com.cmms10.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User; // Spring Security's User class
import java.util.Collection;

public class CustomUserDetails extends User {

    private final String companyId;
    private final String companyName;
    private final String siteId;
    private final String siteName;
    private final String deptId;
    private final String deptName;
    private final String userFullName; // To distinguish from getUsername() which is userId

    public CustomUserDetails(String username, 
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             String companyId,
                             String companyName,
                             String siteId,
                             String siteName,
                             String deptId,
                             String deptName,
                             String userFullName) {
        super(username, password, authorities);
        this.companyId = companyId;
        this.companyName = companyName;
        this.siteId = siteId;
        this.siteName = siteName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.userFullName = userFullName;
    }

    // Getters for the custom fields
    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getFormattedUserInfo() {
        return String.format("%s(%s)/%s(%s)/%s(%s)/%s(%s)",
                companyId, companyName,
                siteId != null ? siteId : "", siteName != null ? siteName : "",
                deptId != null ? deptId : "", deptName != null ? deptName : "",
                getUsername(), userFullName 
        );
    }
}
