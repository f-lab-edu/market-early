package kr.flap.config;

import kr.flap.config.jwt.JWTFilter;
import kr.flap.config.jwt.JWTUtil;
import kr.flap.config.security.LoginFilter;
import kr.flap.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Value("${SPRING_JWT_EXPIRED_TIME}")
  private Long expiredTime; // 10분

  private final AuthenticationConfiguration authenticationConfiguration;
  private final JWTUtil jwtUtil;
  private final UserRepository userRepository;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/v1/users/**").permitAll()
            .requestMatchers("/v1/products/**").permitAll()
            .requestMatchers("/v1/payments/**").permitAll()
            .requestMatchers("/success").permitAll()
            .requestMatchers("/confirm").permitAll()
            .requestMatchers("/style.css").permitAll()
            .anyRequest().authenticated()
    );
    http.addFilterBefore(new JWTFilter(jwtUtil, userRepository), LoginFilter.class);
    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, expiredTime), UsernamePasswordAuthenticationFilter.class);

    http.csrf((auth) -> auth.disable());
    http.formLogin(auth -> auth.disable());
    http.httpBasic(auth -> auth.disable());
    http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
            "https://api.tosspayments.com",
            "https://event.tosspayments.com",
            "https://static.toss.im",
            "https://pages.tosspayments.com",
            "https://polyfill-fe.toss.im",
            "https://assets-fe.toss.im",
            "http://localhost:8080/v1/payments/**"
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ADMIN > USER");
    return roleHierarchy;
  }
}