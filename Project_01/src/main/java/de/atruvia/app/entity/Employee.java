package de.atruvia.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name ="Employees")
public class Employee {

    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Company company;
}
