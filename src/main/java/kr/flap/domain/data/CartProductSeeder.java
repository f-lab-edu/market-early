package kr.flap.domain.data;

import kr.flap.domain.model.cart.CartProduct;
import kr.flap.domain.model.cart.CartProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CartProductSeeder implements JoinTableSeeder<CartProduct> {

  private final CartProductRepository cartProductRepository;

  @Override
  public void setJoinTableList(List<CartProduct> cartProductList) {
    cartProductRepository.saveAll(cartProductList);
  }
}
