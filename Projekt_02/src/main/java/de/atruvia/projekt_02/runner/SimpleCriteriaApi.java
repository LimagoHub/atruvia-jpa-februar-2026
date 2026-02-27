package de.atruvia.projekt_02.runner;

import de.atruvia.projekt_02.entity.Customer;
import de.atruvia.projekt_02.entity.Customer_; // Das generierte Metamodel für Typsicherheit
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Diese Klasse demonstriert die grundlegende Nutzung der JPA Criteria API.
 * Die Criteria API ermöglicht es, Datenbankabfragen rein über Java-Objekte
 * zu definieren, statt SQL- oder JPQL-Strings zu verwenden.
 */
@AllArgsConstructor
public class SimpleCriteriaApi {

    private final EntityManagerFactory entityManagerFactory;

    public void go() {
        /*
         * 1. EntityManager erzeugen (Session/First-Level-Cache)
         * Wir nutzen Try-with-Resources, damit der EntityManager am Ende
         * automatisch geschlossen wird (implementiert AutoCloseable).
         */
        try (EntityManager em = entityManagerFactory.createEntityManager()) {

            // 2. CriteriaBuilder: Die "Fabrik" für unsere Abfragebausteine
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // 3. CriteriaQuery: Das Abfrage-Objekt mit Definition des Rückgabetyps (Customer)
            CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

            // 4. Root: Definiert die Basis-Entität (das "FROM Customer")
            // Über das Root-Objekt greifen wir auf die Attribute der Entität zu.
            Root<Customer> root = cq.from(Customer.class);

            // 5. Select: Legt fest, was zurückgegeben werden soll (hier das ganze Objekt)
            cq.select(root);

            /*
             * 6. Predicates (Filterbedingungen):
             * Wir nutzen das statische Metamodel (Customer_), um Tippfehler bei
             * Spaltennamen zu vermeiden. Customer_.COUNTRY ist typsicher.
             */

            // Startpunkt für eine ODER-Verknüpfung (ein "leeres" FALSE)
           // Predicate pOr = cb.disjunction();

            // Startpunkt für eine UND-Verknüpfung (ein "leeres" TRUE)
            Predicate pAnd = cb.conjunction();

            // Filter hinzufügen: Land muss "Germany" sein
            // Hinweis: Das Metamodel generiert Konstanten für die Entity-Attribute
            pAnd = cb.and(pAnd, cb.equal(root.get(Customer_.country), "Germany"));

            // Beispiel für einen weiteren (optionalen) Filter:
            pAnd = cb.and(pAnd, cb.like(root.get(Customer_.companyName), "A%"));

            // 7. Where: Die gesammelten Bedingungen der Query hinzufügen
            cq.where(pAnd);

            /*
             * 8. Abfrage ausführen:
             * Wir wandeln die CriteriaQuery in eine ausführbare TypedQuery um.
             */
            TypedQuery<Customer> query = em.createQuery(cq);
            List<Customer> results = query.getResultList();

            // 9. Ergebnisausgabe
            System.out.println("Gefundene Kunden in Deutschland: " + results.size());
            results.forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Fehler bei der Criteria-Abfrage: " + e.getMessage());
            e.printStackTrace();
        }
    }
}