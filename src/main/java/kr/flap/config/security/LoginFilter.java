package kr.flap.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.flap.config.jwt.JWTUtil;
import kr.flap.domain.model.user.service.CustomUserDetails;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;

  private final Long expiredTime;

  public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, Long expiredTime) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.expiredTime = expiredTime;
    setFilterProcessesUrl("/v1/users/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    try {
      // HttpServletRequest에서 JSON 본문을 읽고 파싱
      UserLoginCredentials credentials = new ObjectMapper().readValue(request.getInputStream(), UserLoginCredentials.class);

      // Validate email format
      if (!credentials.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) {
        throw new AuthenticationServiceException("Invalid email format");
      }

      // Check password criteria (example: at least 8 characters)
      if (credentials.getPassword().length() < 8) {
        throw new AuthenticationServiceException("Password must be at least 8 characters long");
      }

      // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 한다.
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              credentials.getEmail(), credentials.getPassword());

      // token에 담은 검증을 위한 AuthenticationManager로 전달
      return authenticationManager.authenticate(authToken);
    } catch (IOException e) {
      throw new AuthenticationServiceException("Failed to parse authentication request body");
    }
  }

  //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 된다)
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    String username = customUserDetails.getUsername();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();

    String role = auth.getAuthority();
    String token = jwtUtil.createJwt(username, role, expiredTime);

    // 보안 옵션으로 jwt Token을 Cookie 담아서 Httponly 옵션 설정후 전달
    Cookie cookie = new Cookie("Authorization",  token);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60);
    response.addCookie(cookie);
  }

  //로그인 실패시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String errorDetail;
    if (failed instanceof BadCredentialsException) {
      errorDetail = "The provided credentials are incorrect.";
    } else if (failed instanceof LockedException) {
      errorDetail = "The account is locked.";
    } else if (failed instanceof DisabledException) {
      errorDetail = "The account is disabled.";
    } else {
      errorDetail = "Authentication failed due to an unknown error.";
    }

    String errorMessage = String.format("{\"error\": \"Invalid email or password.\", \"detail\": \"%s\"}", errorDetail);

    try {
      response.getWriter().write(errorMessage);
      response.getWriter().flush();
      response.getWriter().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String obtainEmail(HttpServletRequest request) {
    return request.getParameter("email");
  }

  // 사용자 로그인 자격 증명을 위한 클래스
  private static class UserLoginCredentials {
    private String email;
    private String password;

    // getters and setters
    public String getEmail() {
      return email;
    }
    public String getPassword() {
      return password;
    }
  }
}
