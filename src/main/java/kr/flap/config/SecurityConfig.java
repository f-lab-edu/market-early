package kr.flap.config;

import kr.flap.domain.model.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomUserDetailsService customUserDetailsService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/v1/users/**").permitAll()
            .requestMatchers("/v1/products/**").permitAll()
            .requestMatchers("/v1/payments/**").authenticated()
            .requestMatchers("/success").permitAll()
            .requestMatchers("/confirm").permitAll()
            .requestMatchers("/style.css").permitAll()
            .anyRequest().authenticated()
    );
//    http.apply(formLogin().loginProcessingUrl("/v1/users/login")); //
    http.csrf((auth) -> auth.disable());
    http.exceptionHandling((auth) -> auth.authenticationEntryPoint(customAuthenticationEntryPoint));
    http.userDetailsService(customUserDetailsService);
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
            "https://assets-fe.toss.im"
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

  @Bean
  public AuthenticationManager customAuthenticationManager() throws Exception {
    AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder((ObjectPostProcessor<Object>) customUserDetailsService);
    builder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    return builder.build();
  }
}