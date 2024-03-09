package kr.flap.domain.model.user.service;

import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.dto.UserJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserJoinService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public void userJoinProcess(UserJoinDTO userJoinDTO) {
    Optional<User> email = userRepository.findByEmail(userJoinDTO.getEmail());
    email.ifPresent(user -> {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    });

    User user = User.builder()
            .username(userJoinDTO.getUsername())
            .password(bCryptPasswordEncoder.encode(userJoinDTO.getPassword()))
            .nickname(userJoinDTO.getNickname())
            .email(userJoinDTO.getEmail())
            .mobileNumber(userJoinDTO.getMobileNumber())
            .birthday(userJoinDTO.getBirthday())
            .gender(userJoinDTO.getGender())
            .build();

    userRepository.save(user);
  }
}
