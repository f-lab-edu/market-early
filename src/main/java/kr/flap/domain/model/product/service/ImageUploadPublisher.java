package kr.flap.domain.model.product.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageUploadPublisher {
  private RedisTemplate<String, Object> redisTemplate;
  private ChannelTopic topic;

  public void publish(MultipartFile file) {
    redisTemplate.convertAndSend(topic.getTopic(), file);
  }
}
