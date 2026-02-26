package de.atruvia.projekt_02.runner;

import de.atruvia.projekt_02.entity.Customer;
import de.atruvia.projekt_02.entity.Customer_;
import de.atruvia.projekt_02.entity.Supplier;
import de.atruvia.projekt_02.entity.Supplier_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Demo für Subqueries (Unterabfragen).
 * Ziel: Finde alle Kunden, die in einer Stadt ansässig sind,
 * in der es mindestens einen Lieferanten (Supplier) gibt.
 */
@Slf4j
@AllArgsConstructor
public class SubSelectDemo {

    private final EntityManagerFactory entityManagerFactory;

    public void go() {
        log.info("Starte SubSelectDemo: Kunden in Städten mit Lieferantenpräsenz...");

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // 1. Hauptabfrage (Outer Query) definiert
            CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

            /*
             * 2. Die Subquery (Innere Abfrage)
             * Wir wollen eine Liste von Stadtnamen (Strings) aus der Supplier-Tabelle.
             */
            Subquery<String> subquery = cq.subquery(String.class);
            Root<Supplier> supplierRoot = subquery.from(Supplier.class);
            subquery.select(supplierRoot.get(Supplier_.city)); // Alle Städte der Lieferanten

            /*
             * 3. Die Hauptabfrage (Outer Query)
             */
            Root<Customer> customerRoot = cq.from(Customer.class);

            // Wir nutzen 'distinct', um Duplikate zu vermeiden
            cq.distinct(true);
            cq.select(customerRoot);

            /*
             * 4. Verknüpfung via IN-Klausel
             * WHERE customer.city IN (SELECT supplier.city FROM supplier)
             */
            cq.where(cb.in(customerRoot.get(Customer_.city)).value(subquery));

            // 5. Ausführung
            TypedQuery<Customer> q = em.createQuery(cq);
            List<Customer> customersInSupplierCities = q.getResultList();

            // 6. Ergebnisausgabe
            log.info("Anzahl Kunden in 'Lieferanten-Städten': {}", customersInSupplierCities.size());
            System.out.println("\n--- KUNDEN AN LIEFERANTEN-STANDORTEN ---");
            customersInSupplierCities.forEach(c ->
                    System.out.printf("Kunde: %-25s | Stadt: %s%n", c.getCompanyName(), c.getCity())
            );
            System.out.println("----------------------------------------\n");

        } catch (Exception e) {
            log.error("Fehler bei der Subquery-Ausführung", e);
        }
    }
}