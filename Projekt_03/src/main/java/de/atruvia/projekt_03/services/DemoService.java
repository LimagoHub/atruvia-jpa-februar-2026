package de.atruvia.projekt_03.services;

import de.atruvia.projekt_03.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoService {
    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate transactionTemplate;


    @Transactional
    public void demonstrateFirstLevelCache() {
        // 1. Abfrage: Geht zur DB
        Customer c1 = em.find(Customer.class, "ALFKI");
        // 2. Abfrage: Kommt aus dem First-Level-Cache (kein SQL in der Konsole)
        Customer c2 = em.find(Customer.class, "ALFKI");

        System.out.println(c1 == c2); // true!
    }

    @Transactional
    public List<Customer> getCustomersByCity(String city) {
        return em.createQuery("SELECT c FROM Customer c WHERE c.city = :city", Customer.class)
                .setParameter("city", city)
                // Hier wird der Query-Cache für diese spezifische Abfrage eingeschaltet:
               // .setHint("jakarta.persistence.cache.retrieveMode", "USE")
               // .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }
    /*
    // Zugriff auf die Cache-API von Hibernate
Cache cache = emf.getCache();

// Prüfen, ob eine bestimmte Entität im L2-Cache liegt
boolean isCached = cache.contains(Customer.class, "ALFKI");
System.out.println("Ist ALFKI im L2-Cache? " + isCached);

// Gezielt einen Eintrag aus dem L2-Cache werfen (Invalidierung)
cache.evict(Customer.class, "ALFKI");

// Den kompletten L2-Cache leeren
cache.evictAll();
     */
}