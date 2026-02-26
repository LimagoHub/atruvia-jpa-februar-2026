package de.atruvia.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="tbl_personen")
public class Person extends AbstractEntity{



    @Column(length = 30, nullable = false)
    private String vorname;

    @Column(length = 30, nullable = false)
    private String nachname;

    @Builder.Default
    @Embedded
    private Anschrift anschrift = new Anschrift();

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Kontakt> kontakte = new ArrayList<>();

    @Version
    private long version;
/*
    @Version
    private LocalDateTime timestamp;
*/
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final Person person = (Person) o;
        return getId() != null && Objects.equals(getId(), person.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
