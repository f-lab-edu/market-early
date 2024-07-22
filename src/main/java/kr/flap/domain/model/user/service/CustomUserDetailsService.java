package kr.flap.domain.model.user.service;

import jakarta.transaction.Transactional;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(userEmail)
      .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

    // 미리 필요한 객체를 땡긴다 LazyInitializationException 방지
    user.getRole();
    return new CustomUserDetails(user);
  }
}
