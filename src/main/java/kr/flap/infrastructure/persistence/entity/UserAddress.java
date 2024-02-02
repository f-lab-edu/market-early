package kr.flap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.flap.model.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

import static jakarta.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
}
