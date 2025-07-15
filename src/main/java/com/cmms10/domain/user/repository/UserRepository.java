package com.cmms10.domain.user.repository;

import com.cmms10.domain.user.entity.User;
import com.cmms10.domain.user.entity.UserIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * cmms10 - UserRepository
 * 사용자 정보 저장소
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Repository
public interface UserRepository extends JpaRepository<User, UserIdClass> {

    /** 회사ID로 전체 사용자를 조회합니다.
     * @param companyId 회사 ID
     * @return 사용자 목록
     */
    List<User> findByCompanyIdAndDeleteMarkIsNull(String companyId);
    
    /**
     * 회사 ID와 사용자 이름으로 삭제되지 않은 사용자 정보를 조회합니다.
     * @param companyId 회사 ID
     * @param username 사용자 이름
     * @return 삭제되지 않은 사용자 정보
     */
    Optional<User> findByCompanyIdAndUsernameAndDeleteMarkIsNull(String companyId, String username);
}
