package kr.flap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.flap.model.common.BaseTimeEntity;
import kr.flap.model.order.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
