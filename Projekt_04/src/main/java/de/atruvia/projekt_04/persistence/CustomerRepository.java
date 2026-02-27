package de.atruvia.projekt_04.persistence;

import de.atruvia.projekt_04.persistence.entity.Customer;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;



// Das Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, CustomerRepositoryCustom{

    // Damit der Query-Cache für eine Methode im Repo zieht:
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Customer> findByCompanyName(String companyName);
}