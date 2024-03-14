package kr.flap.domain.model.user.controller;


import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.dto.UserJoinDTO;
import kr.flap.domain.model.user.dto.UserJoinResponseDTO;
import kr.flap.domain.model.user.service.UserJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class JoinController {

  @Autowired
  private UserJoinService joinService;

  @PostMapping("/join")
  public ResponseEntity<UserJoinResponseDTO> join(@RequestBody UserJoinDTO userJoinDTO) {
    User user = joinService.userJoinProcess(userJoinDTO);
    UserJoinResponseDTO responseDTO = new UserJoinResponseDTO(user.getId(), user.getUsername());
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

}
