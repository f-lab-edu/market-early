package kr.flap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.flap.model.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
  private Category category;

  public String name;

  public String brand;

  public String tag;

  @Column(name = "base_price")
  private BigDecimal basePrice;

  @Column(name = "retail_price")
  private BigDecimal retailPrice;

  @Column(name = "discount_rate")
  public Integer discountRate;

  @Column(name = "discount_price")
  public BigDecimal discountPrice;

  private Integer restock;

  @Column(name = "can_restock_notify")
  private Boolean canRestockNotify;

  @Column(name = "min_quantity")
  private Integer minQuantity;

  @Column(name = "max_quantity")
  private Integer maxQuantity;

  @Column(name = "is_sold_out")
  public Boolean isSoldOut;

  @Column(name = "is_purchase_status")
  public Boolean isPurchaseStatus;
}
