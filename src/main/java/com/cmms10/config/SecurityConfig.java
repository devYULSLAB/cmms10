package com.cmms10.config;

import com.cmms10.auth.dto.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> {
                authorize
                    .requestMatchers("/login", "/css/**", "/js/**", "/error", "/favicon.ico").permitAll()
                    .requestMatchers("/").permitAll()
                    .anyRequest().authenticated();
            })
            .formLogin(formLogin -> {
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler(authenticationSuccessHandler())
                    .failureUrl("/login?error")
                    .permitAll();
            })
            .logout(logout -> {
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();
            })
            .authenticationProvider(authenticationProvider());
            
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            HttpSession session = request.getSession();

            session.setAttribute("username", userDetails.getUsername()); 
            session.setAttribute("userFullName", userDetails.getUserFullName());
            session.setAttribute("companyId", userDetails.getCompanyId());
            session.setAttribute("companyName", userDetails.getCompanyName());
            session.setAttribute("siteId", userDetails.getSiteId());
            session.setAttribute("siteName", userDetails.getSiteName());
            session.setAttribute("deptId", userDetails.getDeptId());
            session.setAttribute("deptName", userDetails.getDeptName());
            session.setAttribute("formattedUserInfo", userDetails.getFormattedUserInfo());

            response.sendRedirect("/memo/memoList");
        };
    }
}
