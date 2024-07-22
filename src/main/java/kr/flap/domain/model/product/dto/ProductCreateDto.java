package kr.flap.domain.model.product.dto;

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
public class ProductCreateDto implements Serializable {
  private static final long serialVersionUID = 1L;

  private BigInteger id;

  @Size(max = 100, message = "짧은 설명은 100자를 초과할 수 없습니다.")
  private String shortDescription;

  private LocalDate expirationDate;

  private SellerDto seller;

  private StorageDto storage;

  private List<SubProductDto> subProducts;

  private String mainImageUrl;

  public ProductCreateDto(Product product) {
    this.id = product.getId();
    this.shortDescription = product.getShortDescription();
    this.expirationDate = product.getExpirationDate();
    this.seller = new SellerDto(product.getSeller());
    this.mainImageUrl = product.getMainImageUrl();
    this.storage = new StorageDto(product.getStorage());
    this.subProducts = product.getSubProducts().stream()
            .map(SubProductDto::new)
            .collect(Collectors.toList());
  }
}
