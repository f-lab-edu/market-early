package kr.flap.domain.data;

import kr.flap.domain.model.product.Category;
import kr.flap.domain.model.product.Product;
import kr.flap.domain.model.product.SubProduct;
import kr.flap.domain.model.product.SubProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class SubProductSeeder {

  private final SubProductRepository subProductRepository;
  private final ProductSeeder productSeeder;
  private final CategorySeeder categorySeeder;

  public static int maxSubProducts;
  public static int maxProductSetSubProducts;

  public List<SubProduct> seed() {
    List<Product> products = productSeeder.getProducts();
    Category category = categorySeeder.seed();
    List<SubProduct> subProductList = new ArrayList<>();

    for (Product product : products) {
      IntStream.range(1, maxProductSetSubProducts + 1)
              .mapToObj(i -> createSubProduct(i, product, category))
              .forEach(subProductList::add);
    }

    subProductRepository.saveAll(subProductList);

    return subProductList;
  }

  private SubProduct createSubProduct(int i, Product product, Category category) {
    category.setName(category.getName() + i % 11);
    int tagKeyValue = i % 11;

    // basePrice를 10000 ~ 100000 사이의 랜덤 값으로 설정
    BigDecimal basePrice = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100001));

    // retailPrice는 basePrice의 90%로 설정
    BigDecimal retailPrice = basePrice.multiply(BigDecimal.valueOf(0.9)).setScale(2, RoundingMode.HALF_UP);

    // discountRate는 10 ~ 30% 사이의 랜덤 값으로 설정
    int discountRate = ThreadLocalRandom.current().nextInt(10, 31);

    // discountPrice는 basePrice에서 discountRate%를 뺀 값으로 설정
    BigDecimal discountPrice = basePrice.multiply(BigDecimal.valueOf(1 - (discountRate / 100.0))).setScale(2, RoundingMode.HALF_UP);

    SubProduct subProduct = SubProduct.builder()
            .product(product)
            .category(category)
            .name("상품" + i)
            .brand("브랜드" + i)
            .tag(Map.of("key" + tagKeyValue, "value" + tagKeyValue))
            .basePrice(basePrice)
            .retailPrice(retailPrice)
            .discountRate(discountRate)
            .discountPrice(discountPrice)
            .restock(999)
            .canRestockNotify(true)
            .minQuantity(i)
            .maxQuantity(2 * i)
            .isSoldOut(false)
            .isPurchaseStatus(true)
            .build();

    return subProduct;
  }
}
