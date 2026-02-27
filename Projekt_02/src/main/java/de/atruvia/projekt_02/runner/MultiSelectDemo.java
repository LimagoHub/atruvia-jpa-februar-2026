package de.atruvia.projekt_02.runner;

import de.atruvia.projekt_02.entity.Customer;
import de.atruvia.projekt_02.entity.Customer_;
import de.atruvia.projekt_02.entity.TinyCustomer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Demo zur Verwendung von MultiSelect und DTO-Projektionen.
 * Ziel: Performance-Optimierung, indem nur benötigte Spalten geladen werden.
 */
@Slf4j
@AllArgsConstructor
public class MultiSelectDemo {
    private final EntityManagerFactory entityManagerFactory;

    public void go() {
        log.info("Starte MultiSelectDemo: Projektion auf TinyCustomer DTO...");

        /*
         * 1. Resource Management
         * Der EntityManager wird durch try-with-resources automatisch geschlossen.
         */
        try (EntityManager em = entityManagerFactory.createEntityManager()) {

            CriteriaBuilder cb = em.getCriteriaBuilder();

            // 2. Das Ziel der Query ist nicht die Entity, sondern unser DTO TinyCustomer
            CriteriaQuery<TinyCustomer> cq = cb.createQuery(TinyCustomer.class);

            // 3. Die Datenquelle bleibt jedoch die Customer-Entity
            Root<Customer> root = cq.from(Customer.class);

            /*
             * 4. MultiSelect (Projektion)
             * Hier mappen wir spezifische Datenbankspalten auf den Konstruktor des DTOs.
             * WICHTIG: Die Reihenfolge muss exakt dem Konstruktor von TinyCustomer entsprechen!
             * (z.B. erst CompanyName, dann City)
             */
            cq.multiselect(
                    root.get(Customer_.companyName),
                    root.get(Customer_.city) // Korrigiert: Metamodel ist i.d.R. camelCase
            );

            // 5. Abfrage ausführen
            TypedQuery<TinyCustomer> query = em.createQuery(cq);

            // Optional: Limit setzen für die Demo
            //query.setMaxResults(20);

            List<TinyCustomer> results = query.getResultList();

            // 6. Ergebnisausgabe
            System.out.println("\n--- SCHLANKE KUNDENLISTE (DTO) ---");
            results.forEach(tc ->
                    System.out.printf("Name: %-30s | Ort: %s%n", tc.companyName(), tc.city())
            );
            System.out.println("----------------------------------\n");

        } catch (Exception e) {
            log.error("Fehler bei der Ausführung der MultiSelect-Query", e);
        }
    }
}