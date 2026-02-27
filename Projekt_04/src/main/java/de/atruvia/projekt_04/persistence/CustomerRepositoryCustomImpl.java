package de.atruvia.projekt_04.persistence;

import de.atruvia.projekt_04.persistence.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

// Wichtig: Der Name der Klasse muss auf Impl enden (Standard-Konvention von Spring), damit Spring sie automatisch findet.
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom{


    @PersistenceContext
    private EntityManager entityManager;


    @Transactional // Wichtig für schreibende Operationen
    @Override
    public void simplePersist(final Customer customer) {
        entityManager.persist(customer);
    }
}
