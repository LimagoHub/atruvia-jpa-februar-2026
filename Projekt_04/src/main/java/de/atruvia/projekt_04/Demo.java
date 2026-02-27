package de.atruvia.projekt_04;


import de.atruvia.projekt_04.persistence.CustomerRepository;
import de.atruvia.projekt_04.persistence.entity.Customer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

//@Component
@RequiredArgsConstructor
public class Demo {

    private final CustomerRepository customerRepository;

    @PostConstruct
    private void test(){
        customerRepository.simplePersist(Customer.builder().id("12345").companyName("Test").build());
    }
}
