package de.atruvia.projekt_03.services;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class CacheStatisticsLogger {

    @PersistenceContext
    private EntityManager em;

    public void logStats() {
        // Wir holen uns die Hibernate Session aus dem JPA EntityManager
        Session session = em.unwrap(Session.class);
        Statistics stats = session.getSessionFactory().getStatistics();

        System.out.println("\n--- HIBERNATE CACHE STATS ---");
        System.out.println("L2-Cache Put Count: " + stats.getSecondLevelCachePutCount());
        System.out.println("L2-Cache Hit Count: " + stats.getSecondLevelCacheHitCount());
        System.out.println("L2-Cache Miss Count: " + stats.getSecondLevelCacheMissCount());
        System.out.println("Query-Cache Hit Count: " + stats.getQueryCacheHitCount());
        System.out.println("-----------------------------");
    }
}