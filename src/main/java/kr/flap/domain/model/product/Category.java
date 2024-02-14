package kr.flap.domain.model.product;

import jakarta.persistence.*;
import kr.flap.domain.model.common.BaseTimeEntity;
import lombok.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  public String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  List<SubProduct> subProducts = new ArrayList<>();

  @Builder
  public Category(String name) {
    this.name = name;
  }
}
