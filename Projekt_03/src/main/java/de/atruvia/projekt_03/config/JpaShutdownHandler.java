package de.atruvia.projekt_03.config;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

@Component
public class JpaShutdownHandler {

    private final EntityManagerFactory emf;

    public JpaShutdownHandler(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @PreDestroy
    public void shutdown() {
        if (emf != null && emf.isOpen()) {
            System.out.println("Schließe EntityManagerFactory...");
            emf.close();
        }
    }
}