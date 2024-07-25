package kr.flap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CurlyBackendApplication {

  public static void main(String[] args) {
		SpringApplication.run(CurlyBackendApplication.class, args);
  }
}
