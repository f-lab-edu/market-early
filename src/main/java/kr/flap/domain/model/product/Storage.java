package kr.flap.domain.model.product;

import jakarta.persistence.*;
import kr.flap.domain.model.common.BaseTimeEntity;
import kr.flap.domain.model.product.enums.StorageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "storages")
public class Storage extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @Enumerated(EnumType.STRING)
  public StorageType type;
}
