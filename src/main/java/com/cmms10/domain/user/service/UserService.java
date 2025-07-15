package com.cmms10.domain.user.service;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import com.cmms10.domain.user.repository.UserRepository;
import com.cmms10.domain.user.entity.User;
import com.cmms10.domain.user.entity.UserIdClass;
import java.time.LocalDateTime;

/**
 * cmms10 - UserService
 * 사용자 관리 서비스
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회사 ID로 전체 사용자 정보를 조회합니다.
     * @param companyId 회사 ID
     * @return 사용자 목록
     */
    @Transactional(readOnly = true)
    public java.util.List<User> getAllUsersByCompanyId(String companyId) {
        return userRepository.findByCompanyIdAndDeleteMarkIsNull(companyId);
    }

    /**
     * 사용자 조회 (로그인용)
     * @param companyId 회사 ID
     * @param username 사용자 ID
     * @return 사용자 정보 (삭제되지 않은 사용자만)
     */
    @Transactional(readOnly = true)
    public User getUserByCompanyIdAndUsername(String companyId, String username) {
        return userRepository.findByCompanyIdAndUsernameAndDeleteMarkIsNull(companyId, username)
                .orElseThrow(() -> new RuntimeException("User not found: " + companyId + "/" + username));
    }



    public User saveUser(User user, String username, String mode) {
        LocalDateTime now = LocalDateTime.now();
        // 신규 등록 또는 수정 모드에 따라 처리
        if (mode.equals("new")) {
            // 신규 등록
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new RuntimeException("사용자 ID는 필수입니다.");
            }
            // 중복 체크
            UserIdClass userId = new UserIdClass(user.getCompanyId(), user.getUsername());
            Boolean isDuplicate = userRepository.existsById(userId);
            if (isDuplicate) {
                throw new RuntimeException("삭제되었거나 존재하는 사용자 ID입니다: " + user.getUsername());
            }
            // 신규 등록 값 설정 
            user.setCreateDate(now);
            user.setCreateBy(username);
        } else if (mode.equals("edit")) {
            // 수정
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new RuntimeException("사용자 ID는 필수입니다.");
            }
            user.setUpdateDate(now);
            user.setUpdateBy(username);
        }
        
        return userRepository.save(user);
    }

    public void deleteUser(String companyId, String username, String usernameFromSession) {
        userRepository.findByCompanyIdAndUsernameAndDeleteMarkIsNull(companyId, username).ifPresent(u -> {
            u.setUpdateBy(usernameFromSession);
            u.setUpdateDate(java.time.LocalDateTime.now());
            u.setDeleteMark("Y");
            userRepository.save(u);
        });
    }
}
