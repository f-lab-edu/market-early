package kr.flap.domain.model.cart;

import jakarta.persistence.*;
import kr.flap.domain.model.product.Product;
import kr.flap.domain.model.common.BaseTimeEntity;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
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

  @Builder
  public CartProduct(Cart cart, Product product, int quantity, BigDecimal productPrice) {
    this.cart = cart;
    this.product = product;
    this.quantity = quantity;
    this.productPrice = productPrice;
  }
}
