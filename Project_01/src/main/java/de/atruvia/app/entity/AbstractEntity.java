package de.atruvia.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@MappedSuperclass
public class AbstractEntity {

    @Id
    private UUID id;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID();
    }

    @PreUpdate
    public void preUpdate() {
        // Timestamp setzen
    }
}
