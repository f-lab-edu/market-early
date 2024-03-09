package kr.flap.domain.model.user.controller;

import kr.flap.domain.model.user.dto.UserJoinDTO;
import kr.flap.domain.model.user.service.UserJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserJoinController {

  @Autowired
  private UserJoinService joinService;

  @GetMapping("/join")
  public String join() {
    return "join";
  }

  @PostMapping("/joinProc")
  public String joinProcess(UserJoinDTO joinDTO) {
    System.out.println(joinDTO.getUsername());
    joinService.userJoinProcess(joinDTO);
    return "redirect:/login";
  }
}
