package com.cmms10.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * cmms10 - AuthController
 * 인증 관련 컨트롤러
 * 
 * @author cmms10
 * @since 2024-03-19
 */
@Controller
public class AuthController {

    /**
     * 로그인 화면을 조회합니다.
     * 
     * @return 로그인 화면
     */
    @GetMapping("/login")
    public String login() {
        // Simply returns the view name for the login page
        // The template should be located at /resources/templates/auth/login.html
        return "auth/login";
    }

    /**
     * 루트 URL에 대한 처리를 합니다.
     * 인증된 사용자는 메모 목록으로, 그렇지 않은 사용자는 로그인 페이지로 리다이렉트합니다.
     * 
     * @param auth 인증 정보
     * @return 리다이렉트 URL
     */
    @GetMapping("/")
    public String home(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/memo/memoList"; 
        }
        return "redirect:/login";
    }
}
