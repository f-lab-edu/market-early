package kr.flap.domain.model.product.dto;

import kr.flap.domain.model.product.Seller;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class SellerDto implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private BigInteger id;

  private String name;

  public SellerDto(Seller seller) {
    this.id = seller.getId();
    this.name = seller.getName();
  }
}
