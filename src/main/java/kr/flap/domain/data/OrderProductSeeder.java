package kr.flap.domain.data;

import kr.flap.domain.model.order.OrderProduct;
import kr.flap.domain.model.order.OrderProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class OrderProductSeeder implements JoinTableSeeder<OrderProduct>  {

  private final OrderProductRepository orderProductRepository;

  @Override
  public void setJoinTableList(List<OrderProduct> orderProductList) {
    orderProductRepository.saveAll(orderProductList);
  }
}
