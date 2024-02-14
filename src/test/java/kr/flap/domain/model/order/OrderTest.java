package kr.flap.domain.model.order;

import jakarta.transaction.Transactional;
import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.reserve.ReserveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.flap.factory.FakeDataFactory.*;
import static kr.flap.model.user.UserGrade.*;
import static kr.flap.model.user.UserRole.ADMIN;
import static kr.flap.model.user.UserRole.USER;
import static kr.flap.model.user.UserStatus.ACTIVE;
import static kr.flap.model.user.UserStatus.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
//@DataJpaTest()
@SpringBootTest
@Rollback(value = false)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserAddressRepository userAddressRepository;

  @Autowired
  ReserveRepository reserveRepository;

  private List<User> fixUserData;
  private List<UserAddress> fixAddressData;

  private List<BigInteger> userIdList = new ArrayList<>();

  private List<Reserve> fixReserveData;

  @BeforeEach
  public void setUp() {
    fixUserData = createFixUserData();
    fixAddressData = createFixAddressData();
    fixReserveData = createFixReserveData();

    userRepository.saveAll(fixUserData);
    userAddressRepository.saveAll(fixAddressData);
    reserveRepository.saveAll(fixReserveData);

    assertThat(userRepository.findAll().size()).isEqualTo(3);

    List<User> userList = userRepository.findAll();
    Long userId1 = userList.get(0).getId();
    Long userId2 = userList.get(1).getId();
    Long userId3 = userList.get(2).getId();

    //when
    User user1 = userRepository.findById(BigInteger.valueOf(userId1)).orElseThrow(() -> new IllegalArgumentException("user1 유저가 없습니다."));
    User user2 = userRepository.findById(BigInteger.valueOf(userId2)).orElseThrow(() -> new IllegalArgumentException("user2 유저가 없습니다."));
    User user3 = userRepository.findById(BigInteger.valueOf(userId3)).orElseThrow(() -> new IllegalArgumentException("user3 유저가 없습니다."));

    assertThat(user1).isNotNull();
    assertThat(user2).isNotNull();
    assertThat(user3).isNotNull();

    userIdList.add(BigInteger.valueOf(userId1));
    userIdList.add(BigInteger.valueOf(userId2));
    userIdList.add(BigInteger.valueOf(userId3));

    List<UserAddress> userAddressList = userAddressRepository.findAll();
    BigInteger userAddressId1 = userAddressList.get(0).getId();
    BigInteger userAddressId2 = userAddressList.get(1).getId();
    BigInteger userAddressId3 = userAddressList.get(2).getId();

    UserAddress userAddress1 = userAddressRepository.findById(userAddressId1).orElseThrow(() -> new IllegalArgumentException("userAddress1 유저가 없습니다."));
    UserAddress userAddress2 = userAddressRepository.findById(userAddressId2).orElseThrow(() -> new IllegalArgumentException("userAddress2 유저가 없습니다."));
    UserAddress userAddress3 = userAddressRepository.findById(userAddressId3).orElseThrow(() -> new IllegalArgumentException("userAddress3 유저가 없습니다."));

    assertThat(userAddress1).isNotNull();
    assertThat(userAddress2).isNotNull();
    assertThat(userAddress3).isNotNull();

    userAddress1.setUser(user1);
    userAddress2.setUser(user1);
    userAddress3.setUser(user3);

    userAddressRepository.save(userAddress1);
    userAddressRepository.save(userAddress2);
    userAddressRepository.save(userAddress3);

    List<Reserve> reserveList = reserveRepository.findAll();
    reserveList.get(0).setUser(user1);
    reserveList.get(1).setUser(user2);
    reserveList.get(2).setUser(user3);

    List<Reserve> reserveList1 = reserveRepository.saveAll(reserveList);

    Reserve reserve1 = reserveRepository.findById(reserveList1.get(0).getId()).orElseThrow(() -> new IllegalArgumentException("reserve가 없습니다."));
    Reserve reserve2 = reserveRepository.findById(reserveList1.get(1).getId()).orElseThrow(() -> new IllegalArgumentException("reserve가 없습니다."));
    Reserve reserve3 = reserveRepository.findById(reserveList1.get(2).getId()).orElseThrow(() -> new IllegalArgumentException("reserve가 없습니다."));

    assertThat(reserve1).isNotNull();
    assertThat(reserve2).isNotNull();
    assertThat(reserve3).isNotNull();

  }

  @AfterEach
  public void deleteAllUserData() {
//    userRepository.deleteAll();
//    userAddressRepository.deleteAll();
//    reserveRepository.deleteAll();
  }

  @Test
  @DisplayName("유저 저장 테스트")
  void save() {
    //when
//    userRepository.saveAll(fixUserData);
    //then
    assertThat(userRepository.findAll().size()).isEqualTo(3);
  }
//
  @Test
  @DisplayName("유저 데이터 읽기")
//  @Rollback(value = false)
  void readUser() {

    //when
    User user1 = userRepository.findById(userIdList.get(0)).orElseThrow(() -> new IllegalArgumentException("user1 유저가 없습니다."));
    User user2 = userRepository.findById(userIdList.get(1)).orElseThrow(() -> new IllegalArgumentException("user2 유저가 없습니다."));
    User user3 = userRepository.findById(userIdList.get(2)).orElseThrow(() -> new IllegalArgumentException("user3 유저가 없습니다."));

    assertThat(user1.getEmail()).isEqualTo("test1@test.com");
    assertThat(user1.getGrade()).isEqualTo(BRONZE);
    assertThat(user1.getBirthday()).isEqualTo("1991-01-01");
    assertThat(user1.getRole()).isEqualTo(USER);
    assertThat(user1.getStatus()).isEqualTo(ACTIVE);
    assertThat(user1.getMobileNumber()).isEqualTo("010-1111-1111");
    assertThat(user1.getNickname()).isEqualTo("testUser1");


    assertThat(user2.getEmail()).isEqualTo("test2@test.com");
    assertThat(user2.getGrade()).isEqualTo(SILVER);
    assertThat(user2.getBirthday()).isEqualTo("1992-01-01");
    assertThat(user2.getRole()).isEqualTo(ADMIN);
    assertThat(user2.getStatus()).isEqualTo(INACTIVE);
    assertThat(user2.getMobileNumber()).isEqualTo("010-2222-2222");
    assertThat(user2.getNickname()).isEqualTo("testUser2");
    assertThat(user2.getPassword()).isEqualTo("testpassword2");


    assertThat(user3.getEmail()).isEqualTo("test3@test.com");
    assertThat(user3.getGrade()).isEqualTo(GOLD);
    assertThat(user3.getBirthday()).isEqualTo("1993-01-01");
    assertThat(user3.getRole()).isEqualTo(USER);
    assertThat(user3.getStatus()).isEqualTo(ACTIVE);
    assertThat(user3.getMobileNumber()).isEqualTo("010-3333-3333");
    assertThat(user3.getNickname()).isEqualTo("testUser3");
    assertThat(user3.getPassword()).isEqualTo("testpassword3");

  }

  @Test
  @DisplayName("유저 업데이트")
//  @Rollback(value = false)
  void updateUser() {
    User user1 = userRepository.findById(userIdList.get(0)).orElseThrow(() -> new IllegalArgumentException("user1 유저가 없습니다."));

    user1.setNickname("testUser1Updated");
    user1.setGrade(GOLD);
    user1.setBirthday(LocalDate.parse("1991-02-01"));
    user1.setRole(ADMIN);
    user1.setStatus(INACTIVE);
    user1.setMobileNumber("010-4444-4444");
    user1.setPassword("testpassword1Updated");

    userRepository.save(user1);

    User updateUser = userRepository.findById(BigInteger.valueOf(user1.getId())).orElseThrow(() -> new IllegalArgumentException("user1 유저가 없습니다."));

    assertThat(updateUser.getEmail()).isEqualTo("test1@test.com");
    assertThat(updateUser.getGrade()).isEqualTo(GOLD);
    assertThat(updateUser.getBirthday()).isEqualTo("1991-02-01");
    assertThat(updateUser.getRole()).isEqualTo(ADMIN);
    assertThat(updateUser.getStatus()).isEqualTo(INACTIVE);
    assertThat(updateUser.getMobileNumber()).isEqualTo("010-4444-4444");
    assertThat(updateUser.getNickname()).isEqualTo("testUser1Updated");
    assertThat(updateUser.getPassword()).isEqualTo("testpassword1Updated");

  }

  @Test
  @DisplayName("유저 삭제")
//  @Rollback(value = false)
  void deleteUser() {
    userRepository.deleteById(userIdList.get(0));
    Optional<User> user1 = userRepository.findById(userIdList.get(0));
    assertThat(user1.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("유저 주소 저장")
  @Transactional
  void saveUserAddress() {
    assertThat(userAddressRepository.findAll().size()).isEqualTo(3);
    assertThat(userAddressRepository.findAll().get(0).getUser()).isSameAs(userRepository.findAll().get(0));
  }

  @Test
  @DisplayName("유저 주소 읽기")
  @Transactional
  void readUserAddress() {
    User user1 = userRepository.findById(userIdList.get(0)).orElseThrow(() -> new IllegalArgumentException("user1 유저가 없습니다."));

    UserAddress userAddress1 = userAddressRepository.findUserAddressByUserId(user1.getId()).get(0);
    assertThat(userAddress1).isSameAs(fixAddressData.get(0));
    assertThat(userAddress1.getAddress()).isEqualTo("서울시 강남구");
    assertThat(userAddress1.getAddressDetail()).isEqualTo("역삼동 111-1");
    assertThat(userAddress1.getZipCode()).isEqualTo("11111");

    UserAddress userAddress2 = userAddressRepository.findUserAddressByUserId(user1.getId()).get(1);
    assertThat(userAddress2).isSameAs(fixAddressData.get(1));
    assertThat(userAddress2.getAddress()).isEqualTo("서울시 강남구");
    assertThat(userAddress2.getAddressDetail()).isEqualTo("역삼동 222-2");
    assertThat(userAddress2.getZipCode()).isEqualTo("22222");
  }

  @Test
  @DisplayName("유저 주소 업데이트")
  @Transactional
  void updateUserAddress() {
    User user1 = userRepository.findById(userIdList.get(0)).orElseThrow(() -> new IllegalArgumentException("user1 유저가 없습니다."));

    UserAddress userAddress1 = userAddressRepository.findUserAddressByUserId(user1.getId()).get(0);
    userAddress1.setAddress("서울시 테스트구");
    userAddress1.setAddressDetail("테스트동 111-1");
    userAddress1.setZipCode("00000");

    userAddressRepository.save(userAddress1);
    userAddressRepository.flush();

    UserAddress updateUserAddress = userAddressRepository.findUserAddressByUserId(user1.getId()).get(0);
    assertThat(updateUserAddress.getAddress()).isEqualTo("서울시 테스트구");
    assertThat(updateUserAddress.getAddressDetail()).isEqualTo("테스트동 111-1");
    assertThat(updateUserAddress.getZipCode()).isEqualTo("00000");
  }

  @Test
  @DisplayName("유저 주소 삭제")
  @Transactional
  void deleteUserAddress() {
    User user1 = userRepository.findById(userIdList.get(0)).orElseThrow(() -> new IllegalArgumentException("user1 유저가 없습니다."));

    UserAddress userAddress1 = userAddressRepository.findUserAddressByUserId(user1.getId()).get(0);
    UserAddress userAddress2 = userAddressRepository.findUserAddressByUserId(user1.getId()).get(1);
    userAddressRepository.delete(userAddress1);
    userAddressRepository.delete(userAddress2);
    userAddressRepository.flush();

    assertThat(userAddressRepository.findUserAddressByUserId(user1.getId()).isEmpty()).isTrue();
  }

  @Test
  @DisplayName("예약 읽기")
  @Transactional
  void readReserve() {
    Reserve reserve = reserveRepository.findById(fixReserveData.get(0).getId()).orElseThrow(() -> new IllegalArgumentException("reserve가 없습니다."));
    assertThat(reserve.getAmount()).isEqualTo(BigDecimal.valueOf(10000));
    assertThat(reserve.isValid()).isTrue();
    assertThat(reserve.getUser()).isSameAs(fixUserData.get(0));
  }

  @Test
  @DisplayName("예약 업데이트")
  @Transactional
  void updateReserve() {
    Reserve reserve = reserveRepository.findById(fixReserveData.get(0).getId()).orElseThrow(() -> new IllegalArgumentException("reserve가 없습니다."));
    reserve.setAmount(BigDecimal.valueOf(20000));
    reserve.setValid(false);

    reserveRepository.save(reserve);
    reserveRepository.flush();

    Reserve updateReserve = reserveRepository.findById(fixReserveData.get(0).getId()).orElseThrow(() -> new IllegalArgumentException("reserve가 없습니다."));
    assertThat(updateReserve.getAmount()).isEqualTo(BigDecimal.valueOf(20000));
    assertThat(updateReserve.isValid()).isFalse();
  }

  @Test
  @DisplayName("예약 삭제")
  void deleteReserve() {
    reserveRepository.deleteById(fixReserveData.get(0).getId());
    Optional<Reserve> reserve = reserveRepository.findById(fixReserveData.get(0).getId());
    assertThat(reserve.isEmpty()).isTrue();
  }
}