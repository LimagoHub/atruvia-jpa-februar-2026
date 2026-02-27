package de.atruvia.app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class Bar extends AbstractEntity{

    private String barname;

    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "bar",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH},fetch = jakarta.persistence.FetchType.LAZY)
    private List<BarKeeper> keepers = new ArrayList<>();



    public List<BarKeeper> getKeepers() {
        return Collections.unmodifiableList(keepers);
    }

    public void addKeeper(BarKeeper barKeeper) {
        barKeeper.setBar(this);
        keepers.add(barKeeper);
    }
}
