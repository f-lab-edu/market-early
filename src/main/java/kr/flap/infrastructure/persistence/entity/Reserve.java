package kr.flap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.flap.model.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jakarta.persistence.FetchType.LAZY;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reserves")
@Getter
@Entity
public class Reserve extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "is_valid", nullable = false)
  private boolean isValid;

  private BigDecimal amount;
}
