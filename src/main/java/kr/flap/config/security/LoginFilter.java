package kr.flap.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.flap.config.jwt.JWTUtil;
import kr.flap.domain.model.user.service.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;

  public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    setFilterProcessesUrl("/v1/users/login");
  }

  public LoginFilter(AuthenticationManager authenticationManager, AuthenticationManager authenticationManager1, JWTUtil jwtUtil) {
    super(authenticationManager);
    this.authenticationManager = authenticationManager1;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

    // 클라이언트 요청시 username, password 추출
    String email = obtainEmail(request);
    String password = obtainPassword(request);

    //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야한다.
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

    //token에 담은 검증을 위한 AuthenticationManager로 전달
    return authenticationManager.authenticate(authToken);
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
    String token = jwtUtil.createJwt(username, role, 600 * 600 * 10L);

    response.addHeader("Authorization", "Bearer " + token);
  }

  //로그인 실패시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  private String obtainEmail(HttpServletRequest request) {
    return request.getParameter("email");
  }
}
