package de.atruvia.projekt_02.runner;

import de.atruvia.projekt_02.entity.Customer;
import de.atruvia.projekt_02.entity.Customer_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class DynamicSearchDemo {
    private final EntityManagerFactory entityManagerFactory;

    /**
     * Eine Suche, die flexibel auf Eingaben reagiert.
     * @param country Suchbegriff Land (optional)
     * @param city    Suchbegriff Stadt (optional)
     */
    public void go(String country, String city) {
        log.info("Starte dynamische Suche - Land: {}, Stadt: {}", country, city);

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
            Root<Customer> root = cq.from(Customer.class);

            // Liste für unsere dynamischen Bedingungen
            List<Predicate> predicates = new ArrayList<>();

            // 1. Fall: Land wurde eingegeben
            if (country != null && !country.isBlank()) {
                // Wir nutzen hier einen echten Parameter-Platzhalter
                ParameterExpression<String> pCountry = cb.parameter(String.class, "countryParam");
                predicates.add(cb.equal(root.get(Customer_.country), pCountry));
            }

            // 2. Fall: Stadt wurde eingegeben (als Like-Suche)
            if (city != null && !city.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get(Customer_.city)), city.toLowerCase() + "%"));
            }

            // Alle gesammelten Bedingungen mit UND verknüpfen
            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            TypedQuery<Customer> query = em.createQuery(cq);

            // Parameter-Werte binden, falls sie verwendet wurden
            if (country != null && !country.isBlank()) {
                query.setParameter("countryParam", country);
            }

            List<Customer> results = query.getResultList();
            log.info("{} Treffer gefunden.", results.size());
            results.forEach(c -> System.out.println(c.getCompanyName() + " -> " + c.getCity() + ", " + c.getCountry()));

        } catch (Exception e) {
            log.error("Fehler bei der dynamischen Suche", e);
        }
    }
}