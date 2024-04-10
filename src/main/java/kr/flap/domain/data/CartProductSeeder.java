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
public class CartProductSeeder {

  private final CartProductRepository cartProductRepository;

  public void setCartProductList(List<CartProduct> cartProductList) {
    cartProductRepository.saveAll(cartProductList);
  }
}
