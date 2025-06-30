package com.cmms10.domain.user.repository;

import com.cmms10.domain.user.entity.User;
import com.cmms10.domain.user.entity.UserIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * cmms10 - UserRepository
 * 사용자 정보 저장소
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Repository
public interface UserRepository extends JpaRepository<User, UserIdClass> {
    // Custom finder method to find a user by companyId and username strings,
    // which is more convenient than creating UserIdClass instance every time for lookup.
    Optional<User> findByCompanyIdAndUsername(String companyId, String username);
        
    // 로그인을 위한 사용자 조회 (삭제되지 않은 사용자만)
    Optional<User> findByCompanyIdAndUsernameAndDeleteMarkIsNull(String companyId, String username);
}
