package kr.flap.domain.data;

import kr.flap.config.SeederRange;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class UserSeeder implements BaseSeeder{

  private final UserRepository userRepository;

  @Override
  public void seed() {
    List<User> userList = IntStream.range(1, SeederRange.USER.getRange() + 1)
            .mapToObj(this::createUser)
            .toList();

    userRepository.saveAll(userList);
  }

  public List<User> getUserList() {
    return userRepository.findAll();
  }

  @Override
  public boolean isDataAlreadySeeded() {
    return userRepository.count() > 0;
  }

  private User createUser(int index) {
    return User.builder()
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
