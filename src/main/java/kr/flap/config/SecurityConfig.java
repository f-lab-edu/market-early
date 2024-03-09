package kr.flap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/", "/login", "/loginProc", "/join", "joinProc").permitAll()
            .requestMatchers("/my/**").hasAnyRole( "USER")
            .anyRequest().authenticated()
    );
    http.formLogin((auth) -> auth.loginPage("/login")
            .loginProcessingUrl("/loginProc")
            .permitAll()
    );
    http.csrf((auth) -> auth.disable());
    return http.build();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ADMIN > USER");
    return roleHierarchy;
  }
}