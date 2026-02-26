package de.atruvia.projekt_02.runner;


import de.atruvia.projekt_02.entity.Customer;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor

public class EntityGraphExample {
    private final EntityManagerFactory entityManagerFactory;

    public void go() {
        EntityManager em = entityManagerFactory.createEntityManager();


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> customerRoot = cq.from(Customer.class);
        cq.select(customerRoot);

        EntityGraph entityGraph = em.getEntityGraph("customer-entity-graph-with-orders-employees");

        TypedQuery<Customer> q = em.createQuery(cq);

        q.setHint("javax.persistence.fetchgraph", entityGraph);
        List<Customer> allCustomers = q.getResultList();
        allCustomers.forEach(System.out::println);
        em.close();

    }

}
