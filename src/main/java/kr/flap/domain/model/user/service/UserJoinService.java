package kr.flap.domain.model.user.service;

import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.dto.UserJoinDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
public class UserJoinService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public User userJoinProcess(UserJoinDTO userJoinDTO) {
    Optional<User> email = userRepository.findByEmail(userJoinDTO.getEmail());
    email.ifPresent(user -> {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    });

    String birthday = String.valueOf(userJoinDTO.getBirthday());
    LocalDate parseBirthday = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    log.info("[user-join-thread] kr.flap.domain.model.user.service : 사용자 [{}] 가입 시도", userJoinDTO.getPassword());
    User user = User.builder()
            .username(userJoinDTO.getUsername())
            .password(bCryptPasswordEncoder.encode(userJoinDTO.getPassword()))
            .nickname(userJoinDTO.getNickname())
            .email(userJoinDTO.getEmail())
            .mobileNumber(userJoinDTO.getMobileNumber())
            .birthday(parseBirthday)
            .gender(userJoinDTO.getGender())
            .build();

    userRepository.save(user);
    log.info("[user-join-thread] kr.flap.domain.model.user.service : 사용자 [{}] 가입 성공", user.getUsername());
    return user;
  }
}
