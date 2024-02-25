package kr.flap.domain.model.product;

import jakarta.persistence.*;
import kr.flap.domain.model.common.BaseTimeEntity;
import kr.flap.domain.model.product.enums.StorageType;
import lombok.*;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "storages")
public class Storage extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @Enumerated(EnumType.STRING)
  public StorageType type;

  @Builder
  public Storage(StorageType type) {
    this.type = type;
  }
}
