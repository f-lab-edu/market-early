package kr.flap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.flap.model.common.BaseTimeEntity;
import kr.flap.model.order.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "cart_product")
public class CartProduct extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  public Cart cart;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  public Product product;

  public int quantity;

  @Column(name = "product_price", nullable = false)
  public BigDecimal productPrice;
}
