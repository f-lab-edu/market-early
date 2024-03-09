package kr.flap.domain.model.user.controller;

import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.dto.UserLoginDTO;
import kr.flap.domain.model.user.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@Slf4j
public class LoginController {

  @Autowired
  private UserLoginService loginService;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @PostMapping("/loginProc")
  public ModelAndView loginProcess(@ModelAttribute UserLoginDTO loginDTO) {
    Optional<User> loginUser = loginService.userLoginProcess(loginDTO);

    if(loginUser.isPresent()) {
      return new ModelAndView("redirect:/main");
    } else {
      ModelAndView mv = new ModelAndView("error");
      mv.addObject("message", "로그인 실패");
      return mv;
    }
  }
}
