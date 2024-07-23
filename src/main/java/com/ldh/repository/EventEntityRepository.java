package com.ldh.repository;

import com.ldh.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, String> {

    @Query(value = """
            SELECT c.client_id AS clientId, COALESCE(e.login_count, 0) AS count
            FROM (SELECT client_id FROM client
            WHERE realm_id = :realmId AND full_scope_allowed = 'true') AS c
            LEFT JOIN (SELECT client_id, COUNT(*) AS login_count
                       FROM event_entity
                       WHERE realm_id = :realmId AND type = 'LOGIN'
                       GROUP BY client_id) AS e
            ON c.client_id = e.client_id
            ORDER BY c.client_id
            """, nativeQuery = true)
    List<Object[]> findClientLoginCounts(@Param("realmId") String realmId);

    @Query(value = """
            WITH all_hours AS (
                SELECT generate_series(0, 23) AS event_hour_kst
            ),
            hourly_events AS (
                SELECT 
                    EXTRACT(HOUR FROM TO_TIMESTAMP(event_time / 1000) AT TIME ZONE 'Asia/Seoul') AS event_hour_kst
                FROM 
                    event_entity
                WHERE 
                    type = 'LOGIN'
                    AND client_id LIKE '%' || :clientIdPattern || '%'
            )
            SELECT 
                a.event_hour_kst, 
                COALESCE(COUNT(e.event_hour_kst), 0) AS event_count
            FROM 
                all_hours a
            LEFT JOIN 
                hourly_events e ON a.event_hour_kst = e.event_hour_kst
            GROUP BY 
                a.event_hour_kst
            ORDER BY 
                a.event_hour_kst
            """, nativeQuery = true)
    List<Object[]> countEventsByHour(@Param("clientIdPattern") String clientIdPattern);

    @Query(value = """
        SELECT user_id, COUNT(*) AS count
        FROM event_entity
        WHERE realm_id = :realmId AND type = 'LOGIN'
        GROUP BY user_id
        ORDER BY count DESC
        """, nativeQuery = true)
    List<Object[]> findUserLoginCounts(@Param("realmId") String realmId);

    @Query(value = """
        SELECT error, COUNT(*) AS count
        FROM event_entity
        WHERE type = 'LOGIN_ERROR'
        GROUP BY error
        ORDER BY 2
        """, nativeQuery = true)
    List<Object[]> findLoginErrors();
}