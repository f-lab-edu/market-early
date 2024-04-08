package kr.flap.domain.data;

import kr.flap.domain.model.product.Product;
import kr.flap.domain.model.product.ProductRepository;
import kr.flap.domain.model.product.Seller;
import kr.flap.domain.model.product.Storage;
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
  private final StorageSeeder storageSeeder;
  private final SellerSeeder sellerSeeder;

  public static int maxProducts;

  public void seed() {
    List<Product> productList = IntStream.range(1, maxProducts)
            .mapToObj(this::createProduct)
            .toList();

    productRepository.saveAll(productList);
  }

  public List<Product> getProducts() {
    return productRepository.findAll();
  }

  private Product createProduct(int i) {
    Storage storage = storageSeeder.seed();
    Seller seller = sellerSeeder.seed();
    return Product.builder()
            .storage(storage)
            .seller(seller)
            .shortDescription("Product" + i)
            .expirationDate(LocalDate.of(2999, 12, 31))
            .mainImageUrl("https://www.google.com" + i)
            .build();
  }
}
