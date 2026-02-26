package de.atruvia.projekt_03.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID")
    private Customer customerID;



    @Column(name = "Orderdate")
    private Instant orderDate;

    @Column(name = "Requireddate")
    private Instant requiredDate;

    @Column(name = "Shippeddate")
    private Instant shippedDate;



    @Column(name = "Freight", precision = 10, scale = 4)
    private BigDecimal freight;

    @Column(name = "Shipname", length = 40)
    private String shipName;

    @Column(name = "Shipaddress", length = 60)
    private String shipAddress;

    @Column(name = "Shipcity", length = 15)
    private String shipCity;

    @Column(name = "Shipregion", length = 15)
    private String shipRegion;

    @Column(name = "Shippostalcode", length = 10)
    private String shipPostalCode;

    @Column(name = "Shipcountry", length = 15)
    private String shipCountry;




}