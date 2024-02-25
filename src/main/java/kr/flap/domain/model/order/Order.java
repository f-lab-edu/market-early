package kr.flap.domain.model.order;

import jakarta.persistence.*;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.common.BaseTimeEntity;
import kr.flap.domain.model.order.enums.OrderStatus;
import lombok.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<OrderProduct> orderProductList = new ArrayList<>();

  @Builder
  public Order(User user, Delivery delivery, OrderStatus status) {
    this.user = user;
    this.delivery = delivery;
    this.status = status;
  }
}
