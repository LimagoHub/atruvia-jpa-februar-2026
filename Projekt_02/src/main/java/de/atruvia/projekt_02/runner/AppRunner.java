package de.atruvia.projekt_02.runner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

        private final EntityManagerFactory entityManagerFactory;

        @Override
        public void run(String... args) {
            new SimpleCriteriaApi(entityManagerFactory).go();
            //new MultiSelectDemo(entityManagerFactory).go();
            //new JoinFetchDemo(entityManagerFactory).go();
            //new SubSelectDemo(entityManagerFactory).go();
            //new SubSelectExistsDemo(entityManagerFactory).go();
            //new EntityGraphExample(entityManagerFactory).go();
            //new UmsatzJeKundeDemo(entityManagerFactory).go();
        }

}
