package kr.flap.domain.model.product;

import jakarta.persistence.*;
import kr.flap.domain.model.common.BaseTimeEntity;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "sub_product")
public class SubProduct extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  public Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  public Category category;

  public String name;

  public String brand;

  public String tag;

  @Column(name = "base_price")
  public BigDecimal basePrice;

  @Column(name = "retail_price")
  public BigDecimal retailPrice;

  @Column(name = "discount_rate")
  public Integer discountRate;

  @Column(name = "discount_price")
  public BigDecimal discountPrice;

  public Integer restock;

  @Column(name = "can_restock_notify")
  public Boolean canRestockNotify;

  @Column(name = "min_quantity")
  public Integer minQuantity;

  @Column(name = "max_quantity")
  public Integer maxQuantity;

  @Column(name = "is_sold_out")
  public Boolean isSoldOut;

  @Column(name = "is_purchase_status")
  public Boolean isPurchaseStatus;

  @Builder
  public SubProduct(Product product, Category category, String name, String brand, String tag, BigDecimal basePrice, BigDecimal retailPrice, Integer discountRate, BigDecimal discountPrice, Integer restock, Boolean canRestockNotify, Integer minQuantity, Integer maxQuantity, Boolean isSoldOut, Boolean isPurchaseStatus) {
    this.product = product;
    this.category = category;
    this.name = name;
    this.brand = brand;
    this.tag = tag;
    this.basePrice = basePrice;
    this.retailPrice = retailPrice;
    this.discountRate = discountRate;
    this.discountPrice = discountPrice;
    this.restock = restock;
    this.canRestockNotify = canRestockNotify;
    this.minQuantity = minQuantity;
    this.maxQuantity = maxQuantity;
    this.isSoldOut = isSoldOut;
    this.isPurchaseStatus = isPurchaseStatus;
  }
}
