package de.atruvia.projekt_02.entity;


import jakarta.persistence.*;
import lombok.*;

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
@NamedEntityGraphs(
        {
                @NamedEntityGraph(
                        name = "customer-entity-graph-with-orders-employees",
                        attributeNodes = {
                                //@NamedAttributeNode("orders"),
                                @NamedAttributeNode(value = "orders", subgraph = "order-subgraph"),
                        },
                        subgraphs = {
                                @NamedSubgraph(
                                        name = "order-subgraph",
                                        attributeNodes = {
                                                @NamedAttributeNode("employeeID"),
                                                @NamedAttributeNode("orderDetails")
                                        }

                                )


                        }
                )

        }
)

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

    @OneToMany(mappedBy = "customerID")
    @ToString.Exclude
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

}