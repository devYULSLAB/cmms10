package com.cmms10.domain.roleAuth.repository;

import com.cmms10.domain.roleAuth.entity.RoleAuth;
import com.cmms10.domain.roleAuth.entity.RoleAuthIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleAuthRepository extends JpaRepository<RoleAuth, RoleAuthIdClass> {
    Optional<RoleAuth> findByRoleIdAndAuthGranted(String roleId, String authGranted);
    List<RoleAuth> findByRoleId(String roleId);
}
