package de.atruvia.projekt_04;



import de.atruvia.projekt_04.persistence.entity.Customer;

import de.atruvia.projekt_04.persistence.CustomerRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository repository;
    private final EntityManagerFactory emf;

    /**
     * Holt einen Kunden per ID.
     * Beim 1. Aufruf: SQL-Query sichtbar im Log.
     * Beim 2. Aufruf: Kein SQL, Daten kommen aus dem L2-Cache.
     */
    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Kunde nicht gefunden"));
    }

    /**
     * Liefert die aktuellen Cache-Statistiken als JSON.
     * Ideal für die Live-Demo im Seminar!
     */
    @GetMapping("/stats")
    public Map<String, Object> getCacheStats() {
        // Wir "entpacken" die Hibernate SessionFactory aus der JPA EntityManagerFactory
        Statistics stats = emf.unwrap(SessionFactory.class).getStatistics();

        return Map.of(
                "L2-Cache-Hits", stats.getSecondLevelCacheHitCount(),
                "L2-Cache-Misses", stats.getSecondLevelCacheMissCount(),
                "L2-Cache-Puts", stats.getSecondLevelCachePutCount(),
                "Query-Cache-Hits", stats.getQueryCacheHitCount(),
                "Query-Cache-Misses", stats.getQueryCacheMissCount(),
                "Entity-Names-In-Cache", stats.getSecondLevelCacheRegionNames()
        );
    }

    /**
     * Erlaubt das manuelle Leeren des Caches über die API.
     */
    @DeleteMapping("/cache")
    public String evictCache() {
        emf.getCache().evictAll();
        return "L2-Cache wurde vollständig geleert!";
    }
}