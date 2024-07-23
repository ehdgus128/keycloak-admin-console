package com.ldh.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    private final RestTemplate restTemplate;

    public KeycloakService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getUserInfo(HttpSession session, String url) {
        HttpHeaders headers = new HttpHeaders();
        String accessToken = (String) session.getAttribute("accessToken");
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user info");
        }
    }

    public ResponseEntity<String> unlockUser(HttpSession session, String url) {
        HttpHeaders headers = new HttpHeaders();
        String accessToken = (String) session.getAttribute("accessToken");
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return ResponseEntity.ok("User unlocked successfully");
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to unlock user");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error unlocking user");
        }
    }

    public ResponseEntity<String> getSessionInfo(HttpSession session, String url) {
        HttpHeaders headers = new HttpHeaders();
        String accessToken = (String) session.getAttribute("accessToken");
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching session info");
        }
    }

    public ResponseEntity<String> getOfflineSessions(HttpSession session, String url) {
        HttpHeaders headers = new HttpHeaders();
        String accessToken = (String) session.getAttribute("accessToken");
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching offline sessions");
        }
    }
}
