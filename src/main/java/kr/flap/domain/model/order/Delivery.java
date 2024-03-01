package kr.flap.domain.model.order;

import jakarta.persistence.*;
import kr.flap.domain.model.user.UserAddress;
import kr.flap.domain.model.common.BaseTimeEntity;
import kr.flap.domain.model.order.enums.DeliveryStatus;
import lombok.*;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "deliveries")
public class Delivery extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
  public Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  public UserAddress address;

  @Enumerated(EnumType.STRING)
  @Column(name = "status",nullable = false)
  public DeliveryStatus status;

  @Builder
  public Delivery(Order order, UserAddress address, DeliveryStatus status) {
    this.order = order;
    this.address = address;
    this.status = status;
  }
}
