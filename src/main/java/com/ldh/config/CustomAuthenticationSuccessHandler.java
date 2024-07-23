package com.ldh.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientRepository authorizedClientRepository;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 후 사용자 정보 가져오기
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        System.out.println("idToken : " + oidcUser.getIdToken().getTokenValue());

        // 클라이언트 등록 정보 가져오기
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(realm);
        System.out.println("clientRegistration : " + clientRegistration);

        // OAuth2AuthorizedClientProvider를 사용하여 OAuth2AuthorizedClientManager 초기화
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .clientCredentials()
                .password()
                .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        // OAuth2AuthorizeRequest 생성
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(realm)
                .principal(authentication)
                .attribute(HttpServletRequest.class.getName(), request)
                .attribute(HttpServletResponse.class.getName(), response)
                .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient != null) {
            String accessTokenValue = authorizedClient.getAccessToken().getTokenValue();
            String refreshTokenValue = authorizedClient.getRefreshToken().getTokenValue();

            // user 객체에서 roles 정보 추출
            Map<String, Object> realmAccess = (Map<String, Object>) user.getAttribute("realm_access");
            List<String> rolesList = (List<String>) realmAccess.get("roles");
            String roles = rolesList.stream().collect(Collectors.joining(", "));
            System.out.println("roles : " + roles);

            // accessToken, refreshToken, name, email 세션에 저장
            request.getSession().setAttribute("accessToken", accessTokenValue);
            request.getSession().setAttribute("refreshToken", refreshTokenValue);
            request.getSession().setAttribute("name", user.getAttribute("name"));
            request.getSession().setAttribute("email", user.getAttribute("email"));
            request.getSession().setAttribute("idToken", oidcUser.getIdToken().getTokenValue());
            request.getSession().setAttribute("roles", roles);

            // 세션 ID를 가져와 출력
            String sessionId = request.getSession().getId();
            System.out.println("Session ID : " + sessionId);
            System.out.println("accessTokenValue : " + accessTokenValue);

            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}