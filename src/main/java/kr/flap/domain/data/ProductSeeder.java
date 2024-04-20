package kr.flap.domain.data;

import kr.flap.domain.model.product.Product;
import kr.flap.domain.model.product.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class ProductSeeder {

  private final ProductRepository productRepository;

  public void seed() {
    List<Product> productList = IntStream.range(1, 9001)
            .mapToObj(this::createProduct)
            .toList();

    productRepository.saveAll(productList);
  }

  public void setProduct(Product product) {
    productRepository.save(product);
  }

  public List<Product> getProductList() {
    return productRepository.findAll();
  }

  public boolean isDataAlreadySeeded() {
    return productRepository.count() > 0;
  }

  private Product createProduct(int i) {
    return Product.builder()
            .shortDescription("Product" + i)
            .expirationDate(LocalDate.of(2999, 12, 31))
            .mainImageUrl("https://www.google.com" + i)
            .build();
  }
}
