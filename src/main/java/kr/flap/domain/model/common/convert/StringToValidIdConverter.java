package kr.flap.domain.model.common.convert;

import kr.flap.domain.model.product.validation.ValidId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class StringToValidIdConverter implements Converter<String, ValidId> {
  @Override
  public ValidId convert(String source) {
    ValidId validId = new ValidId();
    validId.setId(BigInteger.valueOf(Long.parseLong(source)));
    return validId;
  }
}
