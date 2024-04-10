package kr.flap.domain.data;

import kr.flap.domain.model.product.Seller;
import kr.flap.domain.model.product.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class SellerSeeder {

  private final SellerRepository sellerRepository;

  public void seed() {
    List<Seller> sellerList = IntStream.range(1, 9001)
            .mapToObj(this::createSeller)
            .toList();

    sellerRepository.saveAll(sellerList);
  }

  public List<Seller> getSellerList() {
    return sellerRepository.findAll();
  }

  private Seller createSeller(int i) {
    return Seller.builder()
            .name("Seller" + i)
            .build();
  }

  public boolean isDataAlreadySeeded() {
    return sellerRepository.count() > 0;
  }
}
