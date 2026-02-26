package de.atruvia.projekt_02.runner;


import de.atruvia.projekt_02.entity.Customer;
import de.atruvia.projekt_02.entity.Customer_;
import de.atruvia.projekt_02.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class JoinDemo {
    private final EntityManagerFactory entityManagerFactory;

    public void go() {
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> customerRoot = cq.from(Customer.class);


        Join<Customer, Order> orderRoot = customerRoot.join(Customer_.ORDERS);

        cq.select(customerRoot);

        TypedQuery<Customer> q = em.createQuery(cq);
        List<Customer> allCustomers = q.getResultList();
        allCustomers.forEach(System.out::println);
        em.close();

    }
}
