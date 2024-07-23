package com.ldh.service;

import com.ldh.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<String> getClientIds() {
        return clientRepository.findClientIdsByRealmIdAndFullScopeAllowed();
    }
}
