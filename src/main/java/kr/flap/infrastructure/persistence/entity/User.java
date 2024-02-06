package kr.flap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import kr.flap.model.common.BaseTimeEntity;
import kr.flap.model.user.UserGender;
import kr.flap.model.user.UserGrade;
import kr.flap.model.user.UserRole;
import kr.flap.model.user.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true, length = 20)
  private String nickname;

  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Enumerated(EnumType.STRING)
  private UserGrade grade;

  @Column(length = 255, nullable = false, unique = true)
  @Email(message = "이메일 형식을 맞춰주세요.")
  private String email;

  private BigInteger defaultDeliveryAddressId;

  @Column(nullable = false)
  private String mobileNumber;

  private LocalDate birthday;

  @Enumerated(EnumType.STRING)
  private UserGender gender;

  //추후에 PasswordEncoder를 사용하여 암호화
  private String password;

  @OneToOne(fetch = FetchType.LAZY)
  private Cart cart;

  @OneToMany(mappedBy = "user")
  private List<UserAddress> userAddressList = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Reserve> reserveList = new ArrayList<>();
}
