package de.atruvia.app.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class BarKeeper extends AbstractEntity{


    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    //@JoinTable(name= "tbl_bar_barkeeper")
    private Bar bar;


    private String keeperName;
}
