package kr.flap.domain.model.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {

  private ProductDto product;
  private SellerDto seller;
  private StorageDto storage;
  private List<SubProductDto> subProducts;

}
