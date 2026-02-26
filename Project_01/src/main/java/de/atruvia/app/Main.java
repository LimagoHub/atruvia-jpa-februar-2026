package de.atruvia.app;


import de.atruvia.app.entity.Bar;
import de.atruvia.app.entity.BarKeeper;
import de.atruvia.app.entity.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.UUID;


public class Main {
    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("atruvia");

    public static void main(String[] args) {

        new Main().run();
    }

    private void run() {


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown Hook is running !");
            entityManagerFactory.close();
        }
        ));

        mergePerson();

    }

    private void createPerson() {
        Person p = Person.builder().vorname("John").nachname("Doe").build();
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();



            entityManager.persist(p);// Persistent und attached



           /* entityManager.getTransaction().commit();
            entityManager.close();

            //d788eff4-ef26-4e98-af20-145b0a7a00f7
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            p = Person.builder().id(UUID.fromString("d788eff4-ef26-4e98-af20-145b0a7a00f7")).vorname("John").nachname("Doe").version(1).build();

            entityManager.merge(p);// Persistent
            */

            entityManager.getTransaction().commit();

        }


    }

    private void mergePerson() {
        // d70c84b3-51d5-483e-9460-52b10be51734
        //Person p = Person.builder().id(UUID.fromString("d70c84b3-51d5-483e-9460-52b10be51734")).vorname("Max").nachname("Mustermann").build();
        Person p = Person.builder().id(UUID.fromString("21056d7b-0fb1-4a82-a7ed-d7308d18f23f")).vorname("John").nachname("Wayne").build();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();


        // Vorsicht : Bei Merge ist nur der Rückgabewert attached
        p = entityManager.merge(p);

        p.setVorname("Erika");

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void removePerson() {
        Person p = Person.builder().id(UUID.fromString("d70c84b3-51d5-483e-9460-52b10be51734")).vorname("John").nachname("Wayne").build();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        p = entityManager.find(Person.class, UUID.fromString("d70c84b3-51d5-483e-9460-52b10be51734"));


        entityManager.remove(p);


        entityManager.getTransaction().commit();
        entityManager.close();

    }

    private void createBarKeeper() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Bar bar = Bar.builder().barname("Milchbar").build();

        entityManager.persist(bar);

        BarKeeper keeper = BarKeeper.builder().bar(bar).keeperName("Jonny").build();


        entityManager.persist(keeper);


        BarKeeper b1 = entityManager.find(BarKeeper.class, keeper.getId());
        System.out.println(b1);

        bar.setBarname("Upps");

        entityManager.getTransaction().commit();
        entityManager.close();


    }

    private void createBar() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Bar bar = Bar.builder().barname("Foobar").build();

        bar.getKeepers().add(BarKeeper.builder().bar(bar).keeperName("Jimmy").build());

        entityManager.persist(bar);

        entityManager.getTransaction().commit();
        entityManager.close();


    }


}