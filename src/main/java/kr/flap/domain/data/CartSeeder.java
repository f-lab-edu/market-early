package kr.flap.domain.data;

import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.cart.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CartSeeder {

  private final CartRepository cartRepository;

  public Cart seed() {
    Cart cart = Cart.builder().build();
    cartRepository.save(cart);

    return cart;
  }
}
