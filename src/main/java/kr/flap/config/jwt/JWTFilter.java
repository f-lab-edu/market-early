package kr.flap.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String requestURI = request.getRequestURI();

    // Actuator 경로에 대한 요청은 건너뛰기
    if(requestURI.startsWith("/actuator")) {
      filterChain.doFilter(request, response);
      return;
    }

    //request에서 Authorization 헤더를 찾음
    String authorization = request.getHeader("Authorization");

    //Authorization 헤더 검출
    if (authorization == null || !authorization.startsWith("Bearer ")) {
//      log.info("Authorization 헤더가 없음");
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorization.split(" ")[1];

    //토큰 소멸 시간 검증
    if (jwtUtil.isExpired(token)) {
      log.info("토큰이 만료됨");
      filterChain.doFilter(request, response);
      return;
    }

    String userEmail = jwtUtil.getUserEmail(token);

    //user Entity 생성
    userRepository.findByEmail(userEmail).ifPresentOrElse(user -> {
      CustomUserDetails customUserDetails = new CustomUserDetails(user);

      // 스프링 시큐리티 인증 토큰 생성
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }, () -> {
      log.error("유저를 찾을 수 없음 :{}", userEmail);
    });

    filterChain.doFilter(request, response);
  }
}
