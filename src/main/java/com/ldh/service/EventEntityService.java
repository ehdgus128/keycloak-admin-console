package com.ldh.service;

import com.ldh.dto.ClientLoginCountDTO;
import com.ldh.dto.UserLoginCountDTO;
import com.ldh.repository.EventEntityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventEntityService {

    private final EventEntityRepository eventEntityRepository;

    @Value("${keycloak.realm-id}")
    private String realmId;

    public EventEntityService(EventEntityRepository eventEntityRepository) {
        this.eventEntityRepository = eventEntityRepository;
    }

    public List<ClientLoginCountDTO> getClientLoginCounts() {
        List<Object[]> results = eventEntityRepository.findClientLoginCounts(realmId);
        return results.stream()
                .map(result -> new ClientLoginCountDTO((String) result[0], ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<Object[]> getEventCountsByHour(String clientIdPattern) {
        return eventEntityRepository.countEventsByHour(clientIdPattern);
    }

    public List<UserLoginCountDTO> getUserLoginCounts() {
        List<Object[]> results = eventEntityRepository.findUserLoginCounts(realmId);
        return results.stream()
                .map(result -> new UserLoginCountDTO((String) result[0], ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<Object[]> getLoginErrors() {
        return eventEntityRepository.findLoginErrors();
    }

}


