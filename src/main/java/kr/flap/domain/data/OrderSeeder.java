package kr.flap.domain.data;

import kr.flap.domain.model.order.Order;
import kr.flap.domain.model.order.OrderRepository;
import kr.flap.domain.model.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Transactional
public class OrderSeeder {

  private final OrderRepository orderRepository;

  public List<Order> seed() {
    List<Order> orderList = IntStream.range(1, 3001)
            .mapToObj(this::createOrder)
            .toList();

    return orderRepository.saveAll(orderList);
  }

  private Order createOrder(int i) {
    return Order.builder()
            .status(OrderStatus.CANTORDER)
            .build();
  }

  public void setOrderList(List<Order> orderList) {
    orderRepository.saveAll(orderList);
  }

  public List<Order> getOrderList() {
    return orderRepository.findAll();
  }

  public boolean isDataAlreadySeeded() {
    return orderRepository.count() > 0;
  }

  public void setOrder(Order order) {
    orderRepository.save(order);
  }
}
