package com.ldh.controller;

import com.ldh.service.ClientService;
import com.ldh.service.EventEntityService;
import com.ldh.service.KeycloakService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class KeycloakController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private EventEntityService eventEntityService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KeycloakService keycloakService;

    @Value("${keycloak.realm}")
    private String realm;

    @GetMapping("/")
    public String dashboard(Model model) {

        List<Object[]> eventCounts = eventEntityService.getEventCountsByHour("resource-server");
        model.addAttribute("eventCounts", eventCounts);
        model.addAttribute("clientList", clientService.getClientIds());

        try {
            String clientLoginCountsJson = objectMapper.writeValueAsString(eventEntityService.getClientLoginCounts());
            model.addAttribute("clientLoginCounts", clientLoginCountsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("clientLoginCounts", "[]");
        }

        try {
            String userLoginCountsJson = objectMapper.writeValueAsString(eventEntityService.getUserLoginCounts());
            model.addAttribute("userLoginCountsJson", userLoginCountsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("userLoginCountsJson", "[]");
        }

        try {
            String loginErrorsJson = objectMapper.writeValueAsString(eventEntityService.getLoginErrors());
            model.addAttribute("loginErrorsJson", loginErrorsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("loginErrorsJson", "[]");
        }

        return "dashboard";
    }

    @GetMapping("/apiInfo")
    public String apiInfo() {
        return "apiInfo";
    }

    @GetMapping("/session")
    public String session() {
        return "session";
    }

    @GetMapping("/log")
    public String log() {
        return "log";
    }

    @GetMapping(path = "/user")
    public String index() {
        return "user";
    }

    @GetMapping(path = "/unauthenticated")
    public String unauthenticatedRequests() {
        return "unauthenticated";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<String> getUserInfo(HttpSession session) {
        String url = "https://172.30.1.132/admin/realms/external/ui-ext/brute-force-user?briefRepresentation=true&first=0&max=11&q=";
        return keycloakService.getUserInfo(session, url);
    }

    @DeleteMapping("/unlockUser/{userId}")
    public ResponseEntity<String> unlockUser(@PathVariable String userId, HttpSession session) {
        String url = String.format("https://172.30.1.132/admin/realms/external/attack-detection/brute-force/users/%s", userId);
        return keycloakService.unlockUser(session, url);
    }

    @GetMapping("/getUserSessions")
    public ResponseEntity<String> getSessionInfo(HttpSession session) {
        String url = "https://172.30.1.132/admin/realms/external/ui-ext/sessions?first=0&max=11&type=REGULAR&search=";
        return keycloakService.getSessionInfo(session, url);
    }

    @GetMapping("/getOfflineSessions")
    public ResponseEntity<String> getOfflineSessions(HttpSession session) {
        String url = "https://172.30.1.132/admin/realms/external/ui-ext/sessions?first=0&max=11&type=OFFLINE&search=";
        return keycloakService.getOfflineSessions(session, url);
    }
}
