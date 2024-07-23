package com.ldh.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/keycloak-logout")
public class KeycloakLogoutController {

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @GetMapping
    public String logout(HttpServletRequest request, HttpSession session) throws IOException {
        // 세션에서 토큰 가져오기
        String idToken = (String) session.getAttribute("idToken");

        // 로그아웃 URL 생성
        String redirectUri = "http://172.30.1.132:8081/logout";  // 실제 로그아웃 리디렉션 URI로 업데이트
        String logoutUrl = String.format("%s/protocol/openid-connect/logout?id_token_hint=%s&post_logout_redirect_uri=%s",
                keycloakUrl, idToken, redirectUri);

        System.out.println("logoutUrl : " + logoutUrl);

        // Keycloak 로그아웃 URL로 리디렉션
        return "redirect:" + logoutUrl;
    }
}
