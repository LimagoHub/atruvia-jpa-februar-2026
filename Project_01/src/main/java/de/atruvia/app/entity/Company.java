package de.atruvia.app.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name ="Companies")
/*
@NamedEntityGraph(
    name = "entityGraph.Company.employees", attributeNodes = @NamedAttributeNode("employees")
            )

 */
//@Cacheable
public class Company {

    @Id
    private Long id;
    private String company;

    private String country;


    private String city;



    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "company")
    private List<Employee> employees = new ArrayList<>();

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final Company company = (Company) o;
        return getId() != null && Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
