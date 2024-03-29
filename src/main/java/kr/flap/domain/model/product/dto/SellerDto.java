package kr.flap.domain.model.product.dto;

import kr.flap.domain.model.product.Seller;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class SellerDto {
  private BigInteger id;

  private String name;

  public SellerDto(Seller seller) {
    this.id = seller.getId();
    this.name = seller.getName();
  }
}
