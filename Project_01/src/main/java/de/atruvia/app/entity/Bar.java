package de.atruvia.app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class Bar extends AbstractEntity{

    private String barname;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "bar",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
    private List<BarKeeper> keepers = new ArrayList<>();

}
