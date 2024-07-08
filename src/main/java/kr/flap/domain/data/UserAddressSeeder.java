package kr.flap.domain.data;

import kr.flap.config.SeederRange;
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
public class UserAddressSeeder implements BaseSeeder{

  private final UserAddressRepository userAddressRepository;

  @Override
  public void seed() {
    List<UserAddress> userAddressList = IntStream.range(1, SeederRange.USER_ADDRESS.getRange() + 1)
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

  @Override
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
