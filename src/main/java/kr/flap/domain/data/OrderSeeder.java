package kr.flap.domain.data;

import kr.flap.domain.model.order.Delivery;
import kr.flap.domain.model.order.DeliveryRepository;
import kr.flap.domain.model.order.Order;
import kr.flap.domain.model.order.OrderRepository;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Transactional
public class OrderSeeder {

  private final OrderRepository orderRepository;
  private final DeliverySeeder deliverySeeder;
  private final UserRepository userRepository;

  public void seed() {
    List<Object> orderList = IntStream.range(1, 101)
            .mapToObj(this::createOrder)
            .toList();
  }

  private Object createOrder(int i) {
    Delivery delivery = deliverySeeder.seed();
    userRepository.findById(BigInteger.valueOf(i)).ifPresent(user -> {
      Order.builder()
              .delivery(delivery)
              .user(user)
              .build();
    });
    return null;
  }
}
