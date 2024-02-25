package kr.flap.domain.model.order;

import jakarta.persistence.*;
import kr.flap.domain.model.common.BaseTimeEntity;
import kr.flap.domain.model.product.Product;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "order_product")
public class OrderProduct extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  public Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  public Product product;

  public int quantity;

  @Column(name = "product_price", nullable = false)
  public BigDecimal productPrice;

  @Builder
  public OrderProduct(Order order, Product product, int quantity, BigDecimal productPrice) {
    this.order = order;
    this.product = product;
    this.quantity = quantity;
    this.productPrice = productPrice;

//    if(order == null && product == null) {
//      throw new IllegalArgumentException("주문과 상품 둘중 하나가 필요합니다.");
//    }
  }
}
