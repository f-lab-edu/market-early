package kr.flap.domain.model.product.dto;

import kr.flap.domain.model.product.Storage;
import kr.flap.domain.model.product.enums.StorageType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class StorageDto implements Serializable {
  private static final long serialVersionUID = 1L;

  private BigInteger id;

  private StorageType type;

  public StorageDto(Storage storage) {
    this.id = storage.getId();
    this.type = storage.getType();
  }
}
