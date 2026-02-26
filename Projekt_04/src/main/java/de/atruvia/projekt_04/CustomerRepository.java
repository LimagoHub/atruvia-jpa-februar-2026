package de.atruvia.projekt_04;

import de.atruvia.projekt_04.entity.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.QueryHint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;



// Das Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    // Damit der Query-Cache für eine Methode im Repo zieht:
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Customer> findByCompanyName(String companyName);
}