package kr.flap.domain.data;

import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import kr.flap.domain.model.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class UserSeeder {

  private final UserRepository userRepository;
  private final CartSeeder cartSeeder;

  public void seed() {
    List<User> userList = IntStream.range(1, 11)
            .mapToObj(this::createUser)
            .toList();

    userRepository.saveAll(userList);
  }

  private User createUser(int index) {
    Cart cart = cartSeeder.seed();
    return User.builder()
            .cart(cart)
            .username("User" + index)
            .nickname("Nickname" + index)
            .status(UserStatus.ACTIVE)
            .role(UserRole.USER)
            .grade(UserGrade.BRONZE)
            .email("user" + index + "@example.com")
            .mobileNumber("010-1234-567" + index)
            .birthday(LocalDate.of(1990, 1, 1).plusDays(index))
            .password("password" + index)
            .gender(UserGender.MALE)
            .build();
  }
}
