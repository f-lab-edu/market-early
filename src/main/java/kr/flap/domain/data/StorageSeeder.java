package kr.flap.domain.data;

import kr.flap.domain.model.product.Storage;
import kr.flap.domain.model.product.StorageRepository;
import kr.flap.domain.model.product.enums.StorageType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class StorageSeeder {

  private final StorageRepository storageRepository;

  public boolean isDataAlreadySeeded() {
    return storageRepository.count() > 0;
  }

  public List<Storage> getStorageList() {
    return storageRepository.findAll();
  }

  public void seed() {
    List<Storage> storageList = IntStream.range(1, 9001)
            .mapToObj(this::createStorage)
            .toList();

    storageRepository.saveAll(storageList);
  }

  public Storage createStorage(int i) {
    return Storage.builder()
            .type(StorageType.REFRIGERATED)
            .build();
  }
}
