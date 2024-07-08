package kr.flap.domain.data;

import kr.flap.config.SeederRange;
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
public class DeliverySeeder implements BaseSeeder{

  private final DeliveryRepository deliveryRepository;

  @Override
  public void seed() {
    List<Delivery> deliveryList = IntStream.range(1, SeederRange.DELIVERY.getRange() + 1)
            .mapToObj(this::createDelivery)
            .toList();

    deliveryRepository.saveAll(deliveryList);
  }

  @Override
  public boolean isDataAlreadySeeded() {
    return deliveryRepository.count() > 0;
  }

  public List<Delivery> getDeliveryList() {
    return deliveryRepository.findAll();
  }

  private Delivery createDelivery(int i) {
    return Delivery.builder()
            .status(DeliveryStatus.COMPLETE)
            .build();
  }
}
