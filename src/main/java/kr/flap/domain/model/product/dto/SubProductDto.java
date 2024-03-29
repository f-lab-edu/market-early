package kr.flap.domain.model.product.dto;

import kr.flap.domain.model.product.SubProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SubProductDto {

  private BigInteger id;
  private String name;
  private String brand;
  private Map<String, Object> tag;
  private BigDecimal basePrice;
  private BigDecimal retailPrice;
  private BigDecimal discountPrice;
  private Integer discountRate;
  private Integer restock;
  private Boolean canRestockNotify;
  private Integer minQuantity;
  private Integer maxQuantity;
  private Boolean isSoldOut;
  private Boolean isPurchaseStatus;

  public SubProductDto(SubProduct subProduct) {
    this.id = subProduct.getId();
    this.name = subProduct.getName();
    this.brand = subProduct.getBrand();
    this.tag = subProduct.getTag();
    this.basePrice = subProduct.getBasePrice();
    this.retailPrice = subProduct.getRetailPrice();
    this.discountPrice = subProduct.getDiscountPrice();
    this.discountRate = subProduct.getDiscountRate();
    this.restock = subProduct.getRestock();
    this.canRestockNotify = subProduct.getCanRestockNotify();
    this.minQuantity = subProduct.getMinQuantity();
    this.maxQuantity = subProduct.getMaxQuantity();
    this.isSoldOut = subProduct.getIsSoldOut();
    this.isPurchaseStatus = subProduct.getIsPurchaseStatus();
  }
}
