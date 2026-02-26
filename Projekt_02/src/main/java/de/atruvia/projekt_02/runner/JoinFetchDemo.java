package de.atruvia.projekt_02.runner;

import de.atruvia.projekt_02.entity.*;
import de.atruvia.projekt_02.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Demo für Joins, Aggregationen (COUNT) und Gruppierung.
 * Ziel: Ermittlung der Bestellanzahl pro Kunde für ein spezifisches Land.
 * * Besonderheit: Verwendung eines DTOs (Data Transfer Object) für das Ergebnis.
 */
@Slf4j
@AllArgsConstructor
public class JoinFetchDemo {
    private final EntityManagerFactory entityManagerFactory;

    public void go() {
        log.info("Starte JoinFetchDemo: Aggregation mit DTO-Projektion...");

        /*
         * Nutze Try-with-Resources für sauberes Ressourcen-Management.
         * Der EntityManager wird am Ende des Blocks automatisch geschlossen.
         */
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // 1. Ziel-Struktur definieren (DTO statt Entity)
            CriteriaQuery<AnzahlBestellungenJeKunde> cq = cb.createQuery(AnzahlBestellungenJeKunde.class);

            // 2. Die Haupt-Entität festlegen (Kunden)
            Root<Customer> customerRoot = cq.from(Customer.class);

            // 3. JOIN zu den Bestellungen (Typsicher über das Metamodel)
            // Wir nutzen einen LEFT JOIN, um auch Kunden ohne Bestellungen zu erfassen
            Join<Customer, Order> orderJoin = customerRoot.join(Customer_.orders, JoinType.LEFT);

            // 4. Aggregations-Funktion: Wir zählen die Einträge im Join-Pfad
            Expression<Long> countExpression = cb.count(orderJoin);

            /*
             * 5. Projektion (SELECT)
             * multiselect füllt den Konstruktor des DTOs in der Reihenfolge der Argumente.
             * 1. String (Firmenname) -> 2. Long (Anzahl)
             */
            cq.multiselect(
                    customerRoot.get(Customer_.companyName),
                    countExpression
            );

            // 6. Gruppierung: Notwendig bei der Verwendung von Aggregationsfunktionen (COUNT)
            // Wir gruppieren nach dem Firmennamen des Kunden
            cq.groupBy(customerRoot.get(Customer_.companyName));

            // 7. Sortierung: Absteigend nach der Anzahl der Bestellungen
            cq.orderBy(cb.desc(countExpression));

            /*
             * 8. Filterung:
             * Wir filtern auf ein Attribut der verknüpften Tabelle (Order).
             */
            Predicate pShipCountry = cb.equal(orderJoin.get(Order_.shipCountry), "Germany");
            cq.where(pShipCountry);

            // 9. Ausführung der typsicheren Abfrage
            TypedQuery<AnzahlBestellungenJeKunde> q = em.createQuery(cq);
            List<AnzahlBestellungenJeKunde> results = q.getResultList();

            // 10. Ergebnisausgabe
            log.info("Gefundene Datensätze: {}", results.size());
            System.out.println("---------------------------------------------------------");
            System.out.printf("%-30s | %s%n", "KUNDE", "ANZAHL BESTELLUNGEN");
            System.out.println("---------------------------------------------------------");

            results.forEach(res -> System.out.printf("%-30s | %d%n",
                    res.comanyName(), // Falls Record: Zugriff via Methode
                    res.anzahl()
            ));
            System.out.println("---------------------------------------------------------");

        } catch (Exception e) {
            log.error("Kritischer Fehler in der JoinFetchDemo: {}", e.getMessage(), e);
        }
    }
}