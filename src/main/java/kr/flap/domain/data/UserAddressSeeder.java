package kr.flap.domain.data;

import kr.flap.domain.model.user.UserAddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserAddressSeeder {

  private final UserAddressRepository userAddressRepository;

  public void seed() {

  }
}
