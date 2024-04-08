package kr.flap.domain.data;

import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.cart.CartRepository;
import kr.flap.domain.model.order.Delivery;
import kr.flap.domain.model.order.DeliveryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class DeliverySeeder {

  private final DeliveryRepository deliveryRepository;

  public Delivery seed() {
    return null;
  }
}
