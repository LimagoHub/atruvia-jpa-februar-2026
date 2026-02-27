package de.atruvia.projekt_03.runner;

import de.atruvia.projekt_03.services.CacheStatisticsLogger;
import de.atruvia.projekt_03.services.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final DemoService demoService;
    private final CacheStatisticsLogger cacheStatisticsLogger;
    @Override
    public void run(final String... args) throws Exception {
        //demoService.demonstrateFirstLevelCache();
        //demoService.demonstrateSecondLevelCache();
        demoService.getCustomersByCity("Berlin").forEach(System.out::println);
        demoService.getCustomersByCity("Berlin").forEach(System.out::println);
        demoService.getCustomersByCity("Mannheim").forEach(System.out::println);


        cacheStatisticsLogger.logStats();
    }
}
