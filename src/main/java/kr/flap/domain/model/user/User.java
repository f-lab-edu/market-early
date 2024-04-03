package kr.flap.domain.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.common.BaseTimeEntity;
import kr.flap.domain.model.order.Order;
import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

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
  @Length(min = 1, max = 20)
  private String username;

  @Column(nullable = false, length = 20)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("'ACTIVE'")
  private UserStatus status;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("'USER'")
  private UserRole role;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("'BRONZE'")
  private UserGrade grade;

  @Column(length = 255, nullable = false, unique = true)
  @Email(message = "이메일 형식을 맞춰주세요.")
  private String email;

  @Column(nullable = false)
  private String mobileNumber;

  private LocalDate birthday;

  @Enumerated(EnumType.STRING)
  private UserGender gender;

  @Column(nullable = false)
  private String password;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Cart cart;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserAddress> userAddressList = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Reserve> reserveList = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orderList = new ArrayList<>();

  @Builder
  public User(String username, String nickname, UserStatus status, UserRole role,
              UserGrade grade, String email, String mobileNumber, LocalDate birthday, String password, UserGender gender) {
    this.username = username;
    this.nickname = nickname;
    this.status = status != null ? status : UserStatus.ACTIVE;
    this.role = role != null ? role : UserRole.USER;
    this.grade = grade != null ? grade : UserGrade.BRONZE;
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
            ", username='" + username + '\'' +
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
