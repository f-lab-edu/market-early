package kr.flap.domain.data;

import kr.flap.config.SeederRange;
import kr.flap.domain.model.product.SubProduct;
import kr.flap.domain.model.product.SubProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class SubProductSeeder implements BaseSeeder{

  private final SubProductRepository subProductRepository;

  @Override
  public void seed() {
    List<SubProduct> subProductList = IntStream.range(1, SeederRange.SUBPRODUCT.getRange() + 1)
            .mapToObj(this::createSubProduct)
            .toList();

    subProductRepository.saveAll(subProductList);
  }

  public List<SubProduct> getSubProductList() {
    return subProductRepository.findAll();
  }

  public void setSubProductList(List<SubProduct> subProductList) {
    subProductRepository.saveAll(subProductList);
  }

  private SubProduct createSubProduct(int i) {
    return SubProduct.builder()
            .name("상품" + i)
            .brand("브랜드" + i)
            .tag(Map.of("key" + i, "value" + i))
            .basePrice(BigDecimal.valueOf(1000L * i))
            .retailPrice(BigDecimal.valueOf(1000L * i))
            .discountRate(i)
            .discountPrice(BigDecimal.valueOf(100L * i))
            .restock(i)
            .canRestockNotify(true)
            .minQuantity(i)
            .maxQuantity(2 * i)
            .isSoldOut(false)
            .isPurchaseStatus(true)
            .build();
  }

  @Override
  public boolean isDataAlreadySeeded() {
    return subProductRepository.count() > 0;
  }

  public void setSubProduct(SubProduct subProduct) {
    subProductRepository.save(subProduct);
  }
}
