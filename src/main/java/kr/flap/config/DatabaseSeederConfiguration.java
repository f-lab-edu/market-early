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
      setSeederValue(args);

      if (!seederService.isDataAlreadySeeded()) {
        log.info("Seeding database...");
        seederService.seedDatabase();
        log.info("ConnectedEntities...");
        seederService.connectEntitiesToEntities();
        log.info("DataSeed End");
      } else {
        log.info("Database already seeded. Skipping seeding process.");
      }
    };
  }

  private void setSeederValue(String[] args) {
    int globalSeederValue = -1;

    for (String arg : args) {
      if (arg.startsWith("seeder=")) {
        try {
          globalSeederValue = Integer.parseInt(arg.split("=")[1]);
        } catch (NumberFormatException e) {
          log.error("Invalid range value for seeder: " + arg.split("=")[1]);
        }
        break; // Exit the loop once the seeder value is found
      }
    }

    if(globalSeederValue != -1) {
      log.info("Global seeder value: " + globalSeederValue);

      SeederRange.CART.setRange(globalSeederValue);
      SeederRange.CATEGORY.setRange(globalSeederValue / 10);
      SeederRange.DELIVERY.setRange(globalSeederValue * 2);
      SeederRange.ORDER.setRange(globalSeederValue * 2);
      SeederRange.PRODUCT.setRange(globalSeederValue * 5);
      SeederRange.RESERVE.setRange(globalSeederValue * 2);
      SeederRange.SELLER.setRange(globalSeederValue * 5);
      SeederRange.STORAGE.setRange(globalSeederValue * 5);
      SeederRange.SUBPRODUCT.setRange(globalSeederValue * 10);
      SeederRange.USER_ADDRESS.setRange(globalSeederValue * 2);
      SeederRange.USER.setRange(globalSeederValue);

    } else {
      log.info("Global seeder value not found. Using default value: " + SeederRange.DEFAULT_VALUE.getRange());
    }
  }
}

