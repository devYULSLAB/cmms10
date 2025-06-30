package com.cmms10.domain.roleAuth.service;

import com.cmms10.domain.roleAuth.entity.RoleAuth;
import com.cmms10.domain.roleAuth.entity.RoleAuthIdClass;
import com.cmms10.domain.roleAuth.repository.RoleAuthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * cmms10 - RoleService
 * 역할 권한 관리 서비스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Service
public class RoleAuthService {

    private final RoleAuthRepository roleAuthRepository;

    public RoleAuthService(RoleAuthRepository roleAuthRepository) {
        this.roleAuthRepository = roleAuthRepository;
    }

    @Transactional(readOnly = true)
    public List<RoleAuth> getRoleAuthByRoleId(String roleId) {
        return roleAuthRepository.findByRoleId(roleId);
    }

    @Transactional
    public RoleAuth saveRoleAuth(RoleAuth roleAuth) {
        // 필수 값 검증
        if (roleAuth.getRoleId() == null || roleAuth.getRoleId().isEmpty() ||
            roleAuth.getAuthGranted() == null || roleAuth.getAuthGranted().isEmpty()) {
            throw new IllegalArgumentException("역할ID, 권한은 필수 입력값입니다.");
        }

        // 기존 권한이 있으면 업데이트, 없으면 신규 저장
        Optional<RoleAuth> existingAuth = roleAuthRepository.findByRoleIdAndAuthGranted(roleAuth.getRoleId(), roleAuth.getAuthGranted());
        if (existingAuth.isPresent()) {
            RoleAuth existing = existingAuth.get();
            existing.setRoleName(roleAuth.getRoleName());
            existing.setAuthGranted(roleAuth.getAuthGranted());
            return roleAuthRepository.save(existing);
        }

        // 새로운 권한 저장
        return roleAuthRepository.save(roleAuth);
    }

    // 추가된 메서드: 역할 ID와 권한으로 조회
    @Transactional(readOnly = true)
    public Optional<RoleAuth> getRoleAuthByRoleIdAndAuthGranted(String roleId, String authGranted) {
        return roleAuthRepository.findByRoleIdAndAuthGranted(roleId, authGranted);
    }

    // 추가된 메서드: 역할 ID와 권한으로 삭제
    @Transactional
    public void deleteRoleAuth(String roleId, String authGranted) {
        roleAuthRepository.deleteById(new RoleAuthIdClass(roleId, authGranted));
    }
}
