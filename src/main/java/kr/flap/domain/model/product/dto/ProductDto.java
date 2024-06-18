package kr.flap.domain.model.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.flap.domain.model.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull(message = "ID는 필수입니다.")
  private BigInteger id;

  @Size(max = 100, message = "짧은 설명은 100자를 초과할 수 없습니다.")
  private String shortDescription;

  @NotNull(message = "만료 날짜는 필수입니다.")
  private LocalDate expirationDate;

  private String mainImageUrl;

  @NotNull(message = "판매자 정보는 필수입니다.")
  private SellerDto seller;

  @NotNull(message = "저장 정보는 필수입니다.")
  private StorageDto storage;

  @NotNull(message = "하위 제품 정보는 필수입니다.")
  private List<SubProductDto> subProducts;

  public ProductDto(Product product) {
    this.id = product.getId();
    this.shortDescription = product.getShortDescription();
    this.expirationDate = product.getExpirationDate();
    this.mainImageUrl = product.getMainImageUrl();
    this.seller = new SellerDto(product.getSeller());
    this.storage = new StorageDto(product.getStorage());
    this.subProducts = product.getSubProducts().stream()
            .map(SubProductDto::new)
            .collect(Collectors.toList());
  }
}
