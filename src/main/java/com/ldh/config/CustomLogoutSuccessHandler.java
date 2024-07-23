package com.ldh.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
public class CustomLogoutSuccessHandler extends SecurityContextLogoutHandler {

//    @Value("${logout-url}")
//    private String logoutUrl;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 기본 로그아웃 처리
        super.logout(request, response, authentication);

        // 로그아웃 시 추가 작업 수행
        if (authentication != null) {
            System.out.println("사용자가 로그아웃했습니다: " + authentication.getName());
        } else {
            System.out.println("알 수 없는 사용자가 로그아웃했습니다.");
        }

        // 추가 작업: 로그아웃 후 리디렉션
//        try {
//            // 서브 서버의 Spring Security 로그아웃 실행
//            response.sendRedirect(logoutUrl);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
