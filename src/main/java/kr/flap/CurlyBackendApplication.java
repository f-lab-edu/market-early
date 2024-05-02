package kr.flap;

import kr.flap.domain.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurlyBackendApplication {

  public static void main(String[] args) {
		SpringApplication.run(CurlyBackendApplication.class, args);
  }
}
