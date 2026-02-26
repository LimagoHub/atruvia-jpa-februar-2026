package de.atruvia.app.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

@Entity
//@DiscriminatorValue("Katze") // Default
@Table(name = "tbl_katzen")
public class Katze extends Tier{

    private Integer anzahlMaeuse;
}
