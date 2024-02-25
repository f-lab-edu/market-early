package kr.flap.domain.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.order.Order;
import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.common.BaseTimeEntity;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, length = 20)
  public String nickname;

  @Enumerated(EnumType.STRING)
  public UserStatus status;

  @Enumerated(EnumType.STRING)
  public UserRole role;

  @Enumerated(EnumType.STRING)
  public UserGrade grade;

  @Column(length = 255, nullable = true, unique = false)
  @Email(message = "이메일 형식을 맞춰주세요.")
  public String email;

  @Column(nullable = false)
  public String mobileNumber;

  public LocalDate birthday;

  @Enumerated(EnumType.STRING)
  public UserGender gender;

  //추후에 PasswordEncoder를 사용하여 암호화
  public String password;

  @OneToOne(fetch = FetchType.LAZY)
  private Cart cart;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserAddress> userAddressList = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Reserve> reserveList = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orderList = new ArrayList<>();

  @Builder
  public User(String nickname, UserStatus status, UserRole role,
              UserGrade grade, String email, String mobileNumber, LocalDate birthday, String password, UserGender gender) {
    this.nickname = nickname;
    this.status = status;
    this.role = role;
    this.grade = grade;
    this.email = email;
    this.mobileNumber = mobileNumber;
    this.birthday = birthday;
    this.password = password;
    this.gender = gender;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", nickname='" + nickname + '\'' +
            ", status=" + status +
            ", role=" + role +
            ", grade=" + grade +
            ", email='" + email + '\'' +
            ", mobileNumber='" + mobileNumber + '\'' +
            ", birthday=" + birthday +
            ", gender=" + gender +
            ", password='" + password + '\'' +
            ", cart=" + cart +
            ", userAddressList=" + userAddressList +
            ", reserveList=" + reserveList +
            '}';
  }
}
