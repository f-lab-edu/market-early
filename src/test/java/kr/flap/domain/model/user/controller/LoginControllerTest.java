package kr.flap.domain.model.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.dto.UserLoginDTO;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import kr.flap.domain.model.user.service.UserLoginService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(LoginController.class)
@Disabled
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserLoginService loginService;

  @Test
  public void testLoginSuccess() throws Exception {
    // 1. Mock 객체 설정 (로그인 성공 시나리오)
    final String email = "test@example.com";
    final String password = "password!@#$P";
    UserLoginDTO userLoginDTO = new UserLoginDTO(email, password);
    User mockUser = User.builder().email(email)
            .grade(UserGrade.BRONZE)
            .gender(UserGender.MALE)
            .birthday(LocalDate.parse("1991-01-01"))
            .role(UserRole.USER)
            .status(UserStatus.ACTIVE)
            .mobileNumber("010-1111-1111")
            .nickname("testUser1")
            .password(password)
            .build();

    User anotherUser = User.builder().email("another@example.com")
            .grade(UserGrade.BRONZE)
            .gender(UserGender.MALE)
            .birthday(LocalDate.parse("1992-02-02"))
            .role(UserRole.USER)
            .status(UserStatus.ACTIVE)
            .mobileNumber("010-1111-1111")
            .nickname("test222")
            .password("password!@#")
            .build();
    Optional<User> optionalUser = Optional.of(mockUser);
    Optional<User> optionalAnotherUser = Optional.of(anotherUser);
    assertThat(optionalUser).isPresent();
    when(loginService.userLoginProcess(any(UserLoginDTO.class))).thenReturn(optionalUser);
//    when(userRepository.findByEmail(userLoginDTO.getEmail())).thenReturn(optionalUser);
//    when(bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), optionalUser.get().getEmail())).thenReturn(true);

    assertThat(loginService.userLoginProcess(userLoginDTO)).isEqualTo(optionalUser);
    assertThat(loginService.userLoginProcess(userLoginDTO)).isNotEqualTo(optionalAnotherUser);

    mockMvc.perform(post("/v1/user/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(userLoginDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(mockUser.getId()));
  }

  @Test
  public void testLoginFailure() throws Exception {
    // 1. Mock 객체 설정 (로그인 실패 시나리오)
    String email = "test@example.com";
    String password = "wrongpassword";
    UserLoginDTO userLoginDTO = new UserLoginDTO(email, password);
    when(loginService.userLoginProcess(userLoginDTO)).thenReturn(Optional.empty());

    mockMvc.perform(post("/v1/user/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(userLoginDTO)))
            .andExpect(status().isUnauthorized());
  }

  private String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(obj);
  }
}