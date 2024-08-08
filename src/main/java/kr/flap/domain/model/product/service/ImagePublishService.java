package kr.flap.domain.model.product.service;

import kr.flap.domain.model.product.dto.ImageUploadMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagePublishService {
  private final RedisTemplate<String, Object> redisTemplate;

  @Value("${redis.stream.key}")
  private String streamKey;

  @Async
  public void publishImageUploadMessage(ImageUploadMessage message) {
    String s = String.valueOf(message.getTimestamp());
    System.out.println("s = " + s);
    try {
      MapRecord<String, String, String> record = MapRecord.create(streamKey, Map.of(
              "productId", message.getProductId(),
              "encodedFile", message.getEncodedFile(),
              "timestamp", String.valueOf(message.getTimestamp())
      ));

      redisTemplate.opsForStream().add(record);
//      log.info("Published image upload message to stream: {}", message);
    } catch (Exception e) {
      log.error("Failed to publish message to stream", e);
    }
  }
}
