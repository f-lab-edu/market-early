package kr.flap.domain.data;

import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.cart.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Transactional
public class CartSeeder {

  private final CartRepository cartRepository;

  public List<Cart> seed() {
    List<Cart> cartList = IntStream.range(1, 1001)
            .mapToObj(this::createCart)
            .toList();

    return cartRepository.saveAll(cartList);
  }

  public List<Cart> getCartList() {
    return cartRepository.findAll();
  }

  public void setCartConnectedUser(Cart cart) {
    cartRepository.save(cart);
  }

  private Cart createCart(int i) {
    return Cart.builder().build();
  }

  public boolean isDataAlreadySeeded() {
    return cartRepository.count() > 0;
  }
}
