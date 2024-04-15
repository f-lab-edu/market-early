package kr.flap.domain.model.product.dto;

import jakarta.validation.constraints.*;
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

  @NotNull(message = "ID는 필수입니다.")
  private BigInteger id;

  @NotBlank(message = "이름은 필수입니다.")
  private String name;

  @NotBlank(message = "브랜드는 필수입니다.")
  private String brand;

  @NotNull(message = "태그는 필수입니다.")
  private Map<String, Object> tag;

  @NotNull(message = "기본 가격은 필수입니다.")
  @DecimalMin(value = "0.0", inclusive = false, message = "기본 가격은 0보다 커야합니다.")
  private BigDecimal basePrice;

  @NotNull(message = "소매 가격은 필수입니다.")
  @DecimalMin(value = "0.0", inclusive = false, message = "소매 가격은 0보다 커야합니다.")
  private BigDecimal retailPrice;

  @DecimalMin(value = "0.0", inclusive = true, message = "할인 가격은 0 이상이어야합니다.")
  private BigDecimal discountPrice;

  @Min(value = 0, message = "할인율은 0 이상이어야합니다.")
  @Max(value = 100, message = "할인율은 100 이하이어야합니다.")
  private Integer discountRate;

  @Min(value = 0, message = "재고는 0 이상이어야합니다.")
  private Integer restock;

  @NotNull(message = "재고 알림은 필수입니다.")
  private Boolean canRestockNotify;

  @Min(value = 1, message = "최소 수량은 1 이상이어야합니다.")
  private Integer minQuantity;

  @Max(value = 1000, message = "최대 수량은 1000 이하이어야합니다.")
  private Integer maxQuantity;

  @NotNull(message = "품절 여부는 필수입니다.")
  private Boolean isSoldOut;

  @NotNull(message = "구매 가능 여부는 필수입니다.")
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
