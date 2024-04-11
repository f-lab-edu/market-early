package kr.flap.domain.model.product.validation;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ValidId {

  @NotNull
  @Digits(integer = 15, fraction = 0)
  private BigInteger id;
}
