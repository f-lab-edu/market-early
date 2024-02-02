package kr.flap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.flap.model.common.BaseTimeEntity;
import kr.flap.model.order.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "deliveries")
public class Delivery extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
  public Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  public UserAddress address;

  @Enumerated(EnumType.STRING)
  @Column(name = "status",nullable = false)
  public DeliveryStatus status;
}
