package com.cmms10.domain.roleAuth.service;

import com.cmms10.domain.roleAuth.entity.RoleAuth;
import com.cmms10.domain.roleAuth.entity.RoleAuthIdClass;
import com.cmms10.domain.roleAuth.repository.RoleAuthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

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

    // 모든 권한 목록 조회
    @Transactional(readOnly = true)
    public List<RoleAuth> getAllRoleAuths() {
        return roleAuthRepository.findAll();
    }

    // RoleId로 권한 목록 조회
    @Transactional(readOnly = true)
    public List<RoleAuth> getRoleAuthByRoleId(String roleId) {
        return roleAuthRepository.findByRoleId(roleId);
    }

    // 추가된 메서드: 역할 ID와 권한으로 조회
    @Transactional(readOnly = true)
    public RoleAuth getRoleAuthByRoleIdAndAuthGranted(String roleId, String authGranted) {
        return roleAuthRepository.findByRoleIdAndAuthGranted(roleId, authGranted)
                .orElseThrow(() -> new EntityNotFoundException("권한 정보를 찾을 수 없습니다: " + roleId + "/" + authGranted));
    }
    
    @Transactional
    public void saveRoleAuth(RoleAuth roleAuth) {
        roleAuthRepository.save(roleAuth);
    }

    @Transactional
    public void deleteById(RoleAuthIdClass id) {
        roleAuthRepository.deleteById(id);
    }
}
