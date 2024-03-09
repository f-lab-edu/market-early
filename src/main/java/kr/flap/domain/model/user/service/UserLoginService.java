package kr.flap.domain.model.user.service;

import jakarta.transaction.Transactional;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.dto.UserLoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserLoginService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public Optional<User> userLoginProcess(UserLoginDTO userLoginDTO) {
    String encodedPassword = bCryptPasswordEncoder.encode(userLoginDTO.getPassword());

    Optional<User> loginUser = userRepository.findByEmailAndPassword(userLoginDTO.getEmail(), encodedPassword);

    loginUser.ifPresentOrElse(user -> log.info("로그인 성공"), () -> log.info("로그인 실패"));

    return loginUser;
  }
}
