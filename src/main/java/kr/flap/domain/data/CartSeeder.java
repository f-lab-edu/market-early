package kr.flap.domain.data;

import kr.flap.config.SeederRange;
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
public class CartSeeder implements BaseSeeder {

  private final CartRepository cartRepository;

  @Override
  public void seed() {
    List<Cart> cartList = IntStream.range(1, SeederRange.CART.getRange() + 1)
            .mapToObj(this::createCart)
            .toList();

   cartRepository.saveAll(cartList);
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

  @Override
  public boolean isDataAlreadySeeded() {
    return cartRepository.count() > 0;
  }
}
