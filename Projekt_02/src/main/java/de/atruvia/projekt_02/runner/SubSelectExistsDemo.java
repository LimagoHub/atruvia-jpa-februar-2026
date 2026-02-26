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
 * Demo für korrelierte Subqueries mit EXISTS.
 * Ziel: Finde Kunden, für die MINDESTENS EIN Lieferant in der gleichen Stadt existiert.
 */
@Slf4j
@AllArgsConstructor
public class SubSelectExistsDemo {

    private final EntityManagerFactory entityManagerFactory;

    public void go() {
        log.info("Starte SubSelectExistsDemo: Korrelierte EXISTS-Abfrage...");

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // 1. Hauptabfrage definieren
            CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
            Root<Customer> customerRoot = cq.from(Customer.class);

            /*
             * 2. Die korrelierte Subquery
             * Sie ist "korreliert", weil sie sich auf customerRoot der äußeren Query bezieht.
             */
            Subquery<Supplier> subquery = cq.subquery(Supplier.class);
            Root<Supplier> supplierRoot = subquery.from(Supplier.class);

            // SELECT 1 ODER SELECT * (bei EXISTS spielt der Rückgabewert keine Rolle)
            subquery.select(supplierRoot);

            /*
             * 3. Die Korrelation (Das Bindeglied)
             * WHERE supplier.city = customer.city
             */
            subquery.where(cb.equal(
                    supplierRoot.get(Supplier_.city),
                    customerRoot.get(Customer_.city) // Zugriff auf äußeres Root!
            ));

            /*
             * 4. EXISTS-Bedingung in der Hauptabfrage
             * SELECT * FROM Customer c WHERE EXISTS (SELECT 1 FROM Supplier s WHERE s.city = c.city)
             */
            cq.select(customerRoot).where(cb.exists(subquery));

            // 5. Ausführung
            TypedQuery<Customer> query = em.createQuery(cq);
            List<Customer> results = query.getResultList();

            // 6. Ausgabe
            log.info("Gefundene Kunden via EXISTS: {}", results.size());
            results.forEach(c -> System.out.println("Match gefunden: " + c.getCompanyName() + " in " + c.getCity()));

        } catch (Exception e) {
            log.error("Fehler in SubSelectExistsDemo", e);
        }
    }
}