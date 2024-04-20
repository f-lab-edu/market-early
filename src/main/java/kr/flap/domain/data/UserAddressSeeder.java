package kr.flap.domain.data;

import kr.flap.domain.model.user.UserAddress;
import kr.flap.domain.model.user.UserAddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class UserAddressSeeder {

  private final UserAddressRepository userAddressRepository;

  public void seed() {
    List<UserAddress> userAddressList = IntStream.range(1, 5001)
            .mapToObj(this::createUserAddress)
            .toList();

    userAddressRepository.saveAll(userAddressList);
  }

  public List<UserAddress> getUserAddresses() {
    return userAddressRepository.findAll();
  }

  public void setUserAddressToUser(List<UserAddress> userAddresses) {
    userAddressRepository.saveAll(userAddresses);
  }

  public boolean isDataAlreadySeeded() {
    return userAddressRepository.count() > 0;
  }

  private UserAddress createUserAddress(int i) {
    UserAddress userAddress = UserAddress.builder()
            .address("주소" + i)
            .addressDetail("상세주소" + i)
            .zipCode("12345" + i)
            .build();
    return userAddress;
  }
}
