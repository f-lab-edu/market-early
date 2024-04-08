package kr.flap.domain.data;

import kr.flap.domain.model.product.Seller;
import kr.flap.domain.model.product.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class SellerSeeder {

  private final SellerRepository sellerRepository;

  public static int maxSellers;

  public Seller seed() {
    List<Seller> sellerList = IntStream.range(1, maxSellers)
            .mapToObj(this::createSeller)
            .toList();

    // Random 객체 생성
    Random random = new Random();

    // sellerList에서 랜덤하게 Seller 선택
    Seller randomSeller = sellerList.get(random.nextInt(sellerList.size()));

    return randomSeller;
  }

  private Seller createSeller(int i) {
    return Seller.builder()
            .name("Seller" + i)
            .build();
  }
}
