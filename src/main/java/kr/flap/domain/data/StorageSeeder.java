package kr.flap.domain.data;

import kr.flap.domain.model.product.Storage;
import kr.flap.domain.model.product.StorageRepository;
import kr.flap.domain.model.product.enums.StorageType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
@AllArgsConstructor
public class StorageSeeder {

  private final StorageRepository storageRepository;

  public Storage seed() {
    Random random = new Random();
    StorageType randomStorageType = StorageType.values()[random.nextInt(StorageType.values().length)];

    Storage storage = Storage.builder()
            .type(randomStorageType)
            .build();
    storageRepository.save(storage);
    return storage;
  }
}
