package kr.flap.domain.model.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.dto.UserLoginDTO;
import kr.flap.domain.model.user.dto.UserLoginResponseDTO;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import kr.flap.domain.model.user.service.UserLoginService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(LoginController.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private LoginController loginController;

  @MockBean
  private UserLoginService loginService;

  @MockBean
  private UserRepository userRepository;

//  @BeforeEach
//  public void securitySetUp(WebApplicationContext webApplicationContext) {
//    this.mockMvc = MockMvcBuilders
//            .webAppContextSetup(webApplicationContext)
//            .apply(SecurityMockMvcConfigurers.springSecurity())
//            .build();
//  }
  @Test
  public void testLoginSuccess() throws Exception {
    // 1. Mock 객체 설정 (로그인 성공 시나리오)
    String email = "test@example.com";
    String password = "password";
    UserLoginDTO userLoginDTO = new UserLoginDTO(email, password);
    User mockUser = User.builder().email("test@example.com")
            .grade(UserGrade.BRONZE)
            .gender(UserGender.MAIL)
            .birthday(LocalDate.parse("1991-01-01"))
            .role(UserRole.USER)
            .status(UserStatus.ACTIVE)
            .mobileNumber("010-1111-1111")
            .nickname("testUser1")
            .password("password")
            .build();
    Optional<User> optionalUser = Optional.of(mockUser);
    when(loginService.userLoginProcess(userLoginDTO)).thenReturn(optionalUser);
    when(userRepository.findByEmail(userLoginDTO.getEmail())).thenReturn(optionalUser);

    Assertions.assertThat(loginService.userLoginProcess(userLoginDTO)).isEqualTo(optionalUser);
    Assertions.assertThat(userRepository.findByEmail(userLoginDTO.getEmail())).isEqualTo(optionalUser);

    mockMvc.perform(post("/v1/user/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(userLoginDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(mockUser.getId()))
            .andExpect(jsonPath("$.username").value(mockUser.getUsername()));
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