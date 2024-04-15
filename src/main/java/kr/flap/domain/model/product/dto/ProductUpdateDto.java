package kr.flap.domain.model.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductUpdateDto {


  private String shortDescription;
  private LocalDate expirationDate;
  private String mainImageUrl;

  public ProductUpdateDto(String shortDescription, LocalDate expirationDate, String mainImageUrl) {
    this.shortDescription = shortDescription;
    this.expirationDate = expirationDate;
    this.mainImageUrl = mainImageUrl;
  }
}
