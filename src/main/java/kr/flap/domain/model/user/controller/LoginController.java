package kr.flap.domain.model.user.controller;


import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.dto.UserLoginDTO;
import kr.flap.domain.model.user.dto.UserLoginResponseDTO;
import kr.flap.domain.model.user.dto.error.UserLoginErrorResponseDTO;
import kr.flap.domain.model.user.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
@Slf4j
public class LoginController {

  @Autowired
  private UserLoginService loginService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
    Optional<User> user = loginService.userLoginProcess(userLoginDTO);
    if (user.isPresent()) {
      UserLoginResponseDTO responseDTO = new UserLoginResponseDTO(user.get().getId(), user.get().getUsername());
      return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    } else {
      UserLoginErrorResponseDTO responseDTO = new UserLoginErrorResponseDTO("로그인 실패했습니다. 이메일 또는 비밀번호를 확인하세요.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
    }
  }
}
