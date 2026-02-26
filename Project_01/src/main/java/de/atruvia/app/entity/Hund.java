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
@Table(name = "tbl_hunde")
//@DiscriminatorValue("Hund") // Default
public class Hund extends Tier{

    private Integer anzahlBrieftraeger;
}
