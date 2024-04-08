package kr.flap.config;

import kr.flap.domain.data.DatabaseSeederService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Slf4j
@Configuration
public class DatabaseSeederConfiguration {

  @Bean
  public CommandLineRunner commandLineRunner(DatabaseSeederService seederService) {
    return args -> {
      if (!seederService.isDataAlreadySeeded()) {
        log.info("Seeding database...");
        seederService.seedDatabase();
      } else {
        log.info("Database already seeded. Skipping seeding process.");
      }
    };
  }
}

