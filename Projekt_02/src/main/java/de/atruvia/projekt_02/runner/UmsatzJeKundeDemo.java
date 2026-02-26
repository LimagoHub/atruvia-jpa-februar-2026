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
 * High-End Demo: Umsatzanalyse je Kunde.
 * Berechnet den Gesamtumsatz (Menge * Preis) pro Kunde,
 * gefiltert nach Land und Mindestumsatz (HAVING).
 */
@Slf4j
@AllArgsConstructor
public class UmsatzJeKundeDemo {
    private final EntityManagerFactory entityManagerFactory;

    public void go() {
        log.info("Starte UmsatzJeKundeDemo: Komplexe Aggregation über 3 Tabellen...");

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // 1. Query-Ziel: Unser DTO UmsatzJeKunde
            CriteriaQuery<UmsatzJeKunde> cq = cb.createQuery(UmsatzJeKunde.class);

            // 2. Startpunkt (Root): Customer
            Root<Customer> customerRoot = cq.from(Customer.class);

            // 3. Verschachtelte Joins: Customer -> Order -> OrderDetail
            // Wir nutzen LEFT JOINS, um keine Daten durch fehlende Relationen zu verlieren
            Join<Customer, Order> orderJoin = customerRoot.join(Customer_.orders, JoinType.LEFT);
            Join<Order, OrderDetail> detailJoin = orderJoin.join(Order_.orderDetails, JoinType.LEFT);

            /*
             * 4. Arithmetik & Aggregation:
             * Umsatz = Summe von (Quantity * UnitPrice)
             */
            // Nutze .as(Double.class), um den Typ sicher zu konfolidieren
            Expression<Double> lineItemTotal = cb.prod(
                    detailJoin.get(OrderDetail_.quantity),
                    detailJoin.get(OrderDetail_.unitPrice)
            ).as(Double.class);

            Expression<Double> sumExpression = cb.sum(lineItemTotal);


            /*
             * 5. Projektion: Mapping auf DTO-Konstruktor
             * multiselect(Name, Umsatz)
             */
            cq.multiselect(
                    customerRoot.get(Customer_.companyName),
                    sumExpression
            );

            // 6. Gruppierung nach Kundenname
            cq.groupBy(customerRoot.get(Customer_.companyName));

            // 7. Sortierung: Umsatz absteigend (Top-Kunden zuerst)
            cq.orderBy(cb.desc(sumExpression));

            /*
             * 8. Filterung (WHERE vs. HAVING):
             * WHERE: Filtert die Rohdaten (Land = USA)
             * HAVING: Filtert das aggregierte Ergebnis (Gesamtumsatz >= 50.000)
             */
            Predicate isUsa = cb.equal(customerRoot.get(Customer_.country), "USA");
            cq.where(isUsa);

            cq.having(cb.ge(sumExpression, 50000.0));

            // 9. Abfrage ausführen
            TypedQuery<UmsatzJeKunde> q = em.createQuery(cq);
            List<UmsatzJeKunde> results = q.getResultList();

            // 10. Ausgabe
            System.out.println("\n--- TOP-KUNDEN USA (Umsatz > 50.000) ---");
            results.forEach(res -> System.out.printf(
                    "Kunde: %-30s | Umsatz: %10.2f €%n",
                    res.companyName(),
                    res.umsatz()
            ));
            System.out.println("----------------------------------------\n");

        } catch (Exception e) {
            log.error("Fehler bei der Umsatz-Aggregation", e);
        }
    }
}