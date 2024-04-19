package kr.flap.domain.model.user.service;

import jakarta.transaction.Transactional;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.dto.UserLoginDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserLoginService {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Optional<User> userLoginProcess(UserLoginDTO userLoginDTO) {
    Optional<User> userOptional = userRepository.findByEmail(userLoginDTO.getEmail());

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
        return userOptional;
      } else {
        log.info("[user-login-thread] kr.flap.domain.model.user.controller : 사용자 [{}] 로그인 실패", userLoginDTO.getEmail());
      }
    } else {
      log.info("[user-login-thread] kr.flap.domain.model.user.controller : 사용자 [{}] 로그인 실패", userLoginDTO.getEmail());
    }

    return Optional.empty();
  }
}
