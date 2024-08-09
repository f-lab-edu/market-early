package kr.flap.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

    // 특정 엔드포인트에 대해서는 JWT 인증을 생략
    if (requestURI.startsWith("/v1/products/imageTest") || requestURI.startsWith("/test/products/redis/image")  ) {
      filterChain.doFilter(request, response);
      return;
    }

    // JWT 토큰을 쿠키에서 추출
    String token = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        log.info("cookie name: {}", cookie.getName());
        if ("Authorization".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }

    // JWT 토큰이 없거나 유효하지 않은 경우 필터 체인 진행
    if (token == null || jwtUtil.isExpired(token)) {
      log.info("토큰이 없거나 만료됨");
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
