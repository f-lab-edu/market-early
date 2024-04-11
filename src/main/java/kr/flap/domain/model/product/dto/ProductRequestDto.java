package kr.flap.domain.model.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {

  @NotNull(message = "제품 정보는 필수입니다.")
  private ProductDto product;

  @NotNull(message = "판매자 정보는 필수입니다.")
  private SellerDto seller;

  @NotNull(message = "저장 정보는 필수입니다.")
  private StorageDto storage;

  @NotNull(message = "하위 제품 정보는 필수입니다.")
  private List<SubProductDto> subProducts;

}
