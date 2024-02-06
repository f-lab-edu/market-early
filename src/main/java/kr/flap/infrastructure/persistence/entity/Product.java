package kr.flap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.flap.model.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "products")
public class Product extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "storage_id")
  public Storage storage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id")
  private Seller seller;

  @Column(name = "product_no")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger productNo;

  @Column(name = "short_description")
  public String shortDescription;

  @Column(name = "expiration_date")
  public LocalDate expirationDate;

  @Column(name = "main_image_url")
  public String mainImageUrl;

  @OneToMany(mappedBy = "product")
  public List<SubProduct> subProducts = new ArrayList<>();
}
