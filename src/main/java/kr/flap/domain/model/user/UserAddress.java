package kr.flap.domain.model.user;

import jakarta.persistence.*;
import kr.flap.domain.model.common.BaseTimeEntity;
import lombok.*;

import java.math.BigInteger;

import static jakarta.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "addresses")
@Entity
public class UserAddress extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String addressDetail;

  private String zipCode;

  @Builder
  public UserAddress(User user, String address, String addressDetail, String zipCode) {
    this.user = user;
    this.address = address;
    this.addressDetail = addressDetail;
    this.zipCode = zipCode;
  }
}
