package kr.flap.domain.data;

import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.reserve.ReserveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Transactional
public class ReserveSeeder {

  private final ReserveRepository reserveRepository;

  public List<Reserve> seed() {
    List<Reserve> reserveList = IntStream.range(1, 5001)
            .mapToObj(this::createReserve)
            .toList();

    return reserveRepository.saveAll(reserveList);
  }

  private Reserve createReserve(int i) {
    return Reserve.builder()
            .isValid(true)
            .amount(BigDecimal.valueOf(1000L * i))
            .build();
  }

  public void setReserveList(List<Reserve> reserveList) {
    reserveRepository.saveAll(reserveList);
  }

  public List<Reserve> getReserveList() {
    return reserveRepository.findAll();
  }

  public boolean isDataAlreadySeeded() {
    return reserveRepository.count() > 0;
  }
}
