package de.atruvia.projekt_03.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.processing.SQL;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "customers")
/*
1. READ_ONLY
Die einfachste und schnellste Strategie.

Anwendungsfall: Daten, die sich nie ändern (z. B. Länderlisten, Währungen, PLZ-Verzeichnisse).

Verhalten: Hibernate erlaubt keine Updates. Wenn die Anwendung versucht, eine solche Entität zu speichern, wird eine Exception geworfen.

Vorteil: Sehr geringer Overhead, da keine Sperren (Locks) oder Konsistenzprüfungen nötig sind.

2. NONSTRICT_READ_WRITE
Ein "lockerer" Umgang mit der Konsistenz.

Anwendungsfall: Daten ändern sich selten, und es ist für die Anwendung nicht kritisch, wenn ein User für ein paar Sekunden noch einen alten Stand sieht.

Verhalten: Es gibt keine harten Sperren auf dem Cache. Wenn zwei Transaktionen gleichzeitig updaten, gewinnt die letzte.

Gefahr: "Stale Data". Ein kleiner Zeitraum der Inkonsistenz zwischen DB und Cache ist möglich.

3. READ_WRITE (Der Standard)
Die sicherste Wahl für normale Geschäftsanwendungen.

Anwendungsfall: Daten werden regelmäßig gelesen und verändert (z. B. Kundenprofile, Stammdaten).

Verhalten: Nutzt ein "Soft-Lock"-Verfahren. Wenn eine Transaktion ein Objekt ändert, wird der Cache-Eintrag gesperrt, bis die Transaktion committed ist.

Vorteil: Verhindert effektiv, dass veraltete Daten gelesen werden.

4. TRANSACTIONAL
Die "Königsklasse" (nur mit speziellen Cache-Providern).

Anwendungsfall: Hochkritische Daten in einer JTA-Umgebung (Full Application Server).

Verhalten: Der Cache arbeitet als vollwertiger Teilnehmer in der verteilten Transaktion (2-Phase-Commit).

Hinweis: Ehcache unterstützt dies im Jakarta-Umfeld zwar, aber es ist deutlich schwerer zu konfigurieren und oft langsamer als READ_WRITE.
 */
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Customer {
    @Id
    @Column(name = "CustomerID", nullable = false, length = 5)
    private String id;

    @Column(name = "Companyname", nullable = false, length = 40)
    private String companyName;

    @Column(name = "Contactname", length = 30)
    private String contactName;

    @Column(name = "Contacttitle", length = 30)
    private String contactTitle;

    @Column(name = "Address", length = 60)
    private String address;

    @Column(name = "City", length = 15)
    private String city;

    @Column(name = "Region", length = 15)
    private String region;

    @Column(name = "PostalCode", length = 10)
    private String postalCode;

    @Column(name = "Country", length = 15)
    private String country;

    @Column(name = "Phone", length = 24)
    private String phone;

    @Column(name = "Fax", length = 24)
    private String fax;
    /*
    1. Das "N+1 Problem" im Cache
    Ein häufiger Fehler  ist der Glaube, dass der Cache auch Beziehungen automatisch mit abdeckt.

    Das Szenario: Ein Customer hat viele Orders.
    Selbst wenn der Customer im L2-Cache liegt, wird Hibernate für die orders-Liste jedes Mal eine SQL-Abfrage absetzen, wenn du customer.getOrders() aufrufst (Lazy Loading).

    Die Lösung: Du musst auch die Collection als "cacheable" markieren.
    Füge in der Klasse Customer.java über dem Feld orders folgende Annotation hinzu:
    */
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "customerID")
    @ToString.Exclude
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

}