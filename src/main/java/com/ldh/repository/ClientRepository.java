package com.ldh.repository;

import com.ldh.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    @Query("SELECT c.clientId FROM Client c WHERE c.realmId = '57598eef-0c50-4cb2-af6d-5999f5cc25a9' AND c.fullScopeAllowed = true ORDER BY c.clientId")
    List<String> findClientIdsByRealmIdAndFullScopeAllowed();
}
