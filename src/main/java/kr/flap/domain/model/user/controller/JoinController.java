package kr.flap.domain.model.user.controller;


import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.dto.UserJoinDTO;
import kr.flap.domain.model.user.dto.UserJoinResponseDTO;
import kr.flap.domain.model.user.service.UserJoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class JoinController {

  @Autowired
  private UserJoinService joinService;

  @PostMapping("/join")
  public ResponseEntity<UserJoinResponseDTO> join(@RequestBody UserJoinDTO userJoinDTO) {
    User user = joinService.userJoinProcess(userJoinDTO);
    UserJoinResponseDTO responseDTO = new UserJoinResponseDTO(user.getId(), user.getUsername());
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @GetMapping("/test")
  public String test() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) principal;
      return "User Authorities: " + userDetails.getAuthorities();
    } else {
      return  "Principal: " + principal.toString();
    }
  }
}
