package de.atruvia.projekt_03.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JPAConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Bean
    public DataSource dataSource() {
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();

        // Basis-Konfiguration
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        config.setDriverClassName("org.h2.Driver");

        // --- HIKARI CONNECTION POOL PERFORMANCE TUNING ---

        // Name des Pools für Logging/JMX - hilft bei der Fehlersuche in Multi-Pool-Umgebungen
        config.setPoolName("Atruvia-JPA-Seminar-Pool");

        // Maximale Anzahl an Verbindungen, die gleichzeitig zur DB offen sein dürfen.
        // Faustformel: (2 * Anzahl CPU-Kerne) + Anzahl Festplatten
        config.setMaximumPoolSize(10);

        // Mindestanzahl an "Leerlauf"-Verbindungen, die sofort bereitstehen.
        // Verhindert Latenz beim ersten Request nach einer Pause.
        config.setMinimumIdle(2);

        // Zeit in ms, die ein Thread auf eine Verbindung wartet, bevor eine Exception fliegt.
        // Verhindert, dass die Anwendung bei DB-Überlastung komplett "hängt".
        config.setConnectionTimeout(30000); // 30 Sekunden

        // Maximale Lebensdauer einer Verbindung im Pool.
        // Sollte immer etwas kürzer sein als das Timeout auf Datenbank-Seite (z.B. 30 Min).
        config.setMaxLifetime(1800000); // 30 Minuten

        // Zeit, nach der eine ungenutzte Verbindung (über minimumIdle hinaus) geschlossen wird.
        config.setIdleTimeout(600000); // 10 Minuten

        // --- OPTIMIERUNGEN FÜR PREPARED STATEMENTS (Treiber-abhängig) ---
        // Diese Settings verbessern die Performance, indem sie SQL-Statements serverseitig cachen.
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new com.zaxxer.hikari.HikariDataSource(config);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan("de.atruvia");
        factory.setDataSource(dataSource);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", ddlAuto);
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.format_sql", "true");


        // --- SECOND LEVEL CACHE KONFIGURATION ---
        // 1. Aktivierung
        jpaProperties.put("hibernate.cache.use_second_level_cache", "true");
        // 2. Query Cache Aktivierung
        jpaProperties.put("hibernate.cache.use_query_cache", "true");
        // 3. JCache Region Factory (Standard für Hibernate 6)
        jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
        // 4. Statistiken (extrem wichtig für die Demo!)
        jpaProperties.put("hibernate.generate_statistics", "true");

        jpaProperties.put("hibernate.cache.use_second_level_cache", "true");
        jpaProperties.put("hibernate.cache.use_query_cache", "true");
        jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.internal.JCacheRegionFactory");


        jpaProperties.put("jakarta.persistence.cache.uri", "ehcache.xml");

        // Explizite Angabe des Providers (verhindert Such-Fehler)
        //jpaProperties.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");


        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();

        return factory.getObject();
    }
}