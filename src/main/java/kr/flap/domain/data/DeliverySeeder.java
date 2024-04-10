package kr.flap.domain.data;

import kr.flap.domain.model.order.Delivery;
import kr.flap.domain.model.order.DeliveryRepository;
import kr.flap.domain.model.order.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Transactional
public class DeliverySeeder {

  private final DeliveryRepository deliveryRepository;

  public void seed() {
    List<Delivery> deliveryList = IntStream.range(1, 3001)
            .mapToObj(this::createDelivery)
            .toList();

    deliveryRepository.saveAll(deliveryList);
  }

  public boolean isDataAlreadySeeded() {
    return deliveryRepository.count() > 0;
  }

  public List<Delivery> getDeliveryList() {
    return deliveryRepository.findAll();
  }

  public void setDeliveryList(List<Delivery> deliveryList) {
    deliveryRepository.saveAll(deliveryList);
  }

  private Delivery createDelivery(int i) {
    return Delivery.builder()
            .status(DeliveryStatus.COMPLETE)
            .build();
  }
}
