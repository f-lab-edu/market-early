package kr.flap.domain.model.product;

import jakarta.persistence.*;
import kr.flap.domain.model.common.BaseTimeEntity;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage extends BaseTimeEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "etag", nullable = false)
  private String eTag;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;
}
