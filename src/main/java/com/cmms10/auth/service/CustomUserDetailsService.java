package com.cmms10.auth.service;

import com.cmms10.domain.user.entity.User;
import com.cmms10.domain.user.repository.UserRepository;
import com.cmms10.domain.roleAuth.entity.RoleAuth;
import com.cmms10.domain.roleAuth.repository.RoleAuthRepository;
import com.cmms10.domain.company.entity.Company;
import com.cmms10.domain.company.repository.CompanyRepository;
import com.cmms10.domain.dept.entity.Dept;
import com.cmms10.domain.dept.repository.DeptRepository;
import com.cmms10.auth.dto.CustomUserDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * cmms10 - CustomUserDetailsService
 * Spring Security의 UserDetailsService 구현체
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleAuthRepository roleAuthRepository;
    private final CompanyRepository companyRepository;
    private final DeptRepository deptRepository;
    // default company ID
    // private static final String DEFAULT_COMPANY_ID = "C0001";
    @Value("${cmms10.default-company-id}")
    private String defaultCompanyId;

    public CustomUserDetailsService(
            UserRepository userRepository,
            RoleAuthRepository roleAuthRepository,
            CompanyRepository companyRepository,
            DeptRepository deptRepository) {
        this.userRepository = userRepository;
        this.roleAuthRepository = roleAuthRepository;
        this.companyRepository = companyRepository;
        this.deptRepository = deptRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== CustomUserDetailsService.loadUserByUsername ===");
        System.out.println("Attempting to load user with username: " + username);

        // 사용자 정보 조회 (테스트 목적이므로 password 평문)
        User user = userRepository.findByCompanyIdAndUsernameAndDeleteMarkIsNull(defaultCompanyId, username)
                .orElseThrow(() -> {
                    System.out.println("User not found - CompanyId: " + defaultCompanyId + ", Username: " + username);
                    return new UsernameNotFoundException(
                            "User not found with companyId: " + defaultCompanyId + " and username: " + username);
                });
        System.out.println("User found: " + user.getUsername() + ", Full Name: " + user.getUserFullName());

        // 권한 동적 조회
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRoleId() != null) {
            try {
                // roleId로 여러 권한(복수 row) 조회
                List<RoleAuth> roleAuthList = roleAuthRepository.findByRoleId(user.getRoleId());
                if (!roleAuthList.isEmpty()) {
                    for (RoleAuth roleAuth : roleAuthList) {
                        // authGranted에 콤마로 여러 권한이 있을 수 있음
                        for (String role : roleAuth.getAuthGranted().split(",")) {
                            String trimmed = role.trim();
                            if (!trimmed.isEmpty()) {
                                authorities.add(new SimpleGrantedAuthority(trimmed));
                            }
                        }
                    }
                } else {
                    System.out.println(
                            "Warning: No role auth found for roleId: " + user.getRoleId() + ", using default role");
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }
            } catch (Exception e) {
                System.out.println(
                        "Warning: Error loading role auth for roleId: " + user.getRoleId() + ", using default role");
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
        } else {
            // 기본 권한
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        System.out.println("Granted authorities: " + authorities);

        // 회사 정보 조회
        Company company = companyRepository.findByCompanyIdAndDeleteMarkIsNull(user.getCompanyId())
                .orElseThrow(() -> new UsernameNotFoundException("Company not found with ID: " + user.getCompanyId()));

        // 부서 정보 조회
        Dept dept = null;
        if (user.getDeptId() != null) {
            try {
                dept = deptRepository.findByCompanyIdAndDeptIdAndDeleteMarkIsNull(user.getCompanyId(), user.getDeptId())
                        .orElse(null);
            } catch (Exception e) {
                System.out.println(
                        "Warning: Dept not found with ID: " + user.getDeptId() + ", continuing without dept info");
                dept = null;
            }
        }

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.getCompanyId(),
                company.getCompanyName(),
                user.getDeptId(),
                dept != null ? dept.getDeptName() : "",
                user.getUserFullName());
        System.out.println("Created CustomUserDetails for: " + userDetails.getUsername());
        System.out.println("===============================================");
        return userDetails;
    }
}
