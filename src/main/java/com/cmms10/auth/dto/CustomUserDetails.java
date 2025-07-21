package com.cmms10.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User; // Spring Security's User class
import java.util.Collection;

public class CustomUserDetails extends User {

    private final String companyId;
    private final String companyName;
    private final String deptId;
    private final String deptName;
    private final String userFullName; // To distinguish from getUsername() which is userId

    public CustomUserDetails(String username, 
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             String companyId,
                             String companyName,
                             String deptId,
                             String deptName,
                             String userFullName) {
        super(username, password, authorities);
        this.companyId = companyId;
        this.companyName = companyName;
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

    public String getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    // 주요 기능: 사용자 정보 포맷 반환
    // @return 회사(이름)/부서(이름)/아이디(이름) 형태 문자열
    public String getFormattedUserInfo() {
        return String.format("%s(%s)/%s(%s)/%s(%s)",
                companyId, companyName,
                deptId != null ? deptId : "", deptName != null ? deptName : "",
                getUsername(), userFullName
        );
    }
}
