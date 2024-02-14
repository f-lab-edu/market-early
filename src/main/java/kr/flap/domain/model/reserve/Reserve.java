package kr.flap.domain.model.reserve;

import jakarta.persistence.*;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.common.BaseTimeEntity;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jakarta.persistence.FetchType.LAZY;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reserves")
@Getter
@Setter
@Entity
public class Reserve extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "is_valid", nullable = false)
  public boolean isValid;

  public BigDecimal amount;

  @Builder
  public Reserve(User user, boolean isValid, BigDecimal amount) {
    this.user = user;
    this.isValid = isValid;
    this.amount = amount;
  }
}
