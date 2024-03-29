package kr.flap.domain.model.product.dto;

import kr.flap.domain.model.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {


  private BigInteger id;
  private String shortDescription;
  private LocalDate expirationDate;
  private String mainImageUrl;
  private SellerDto seller;
  private StorageDto storage;
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
