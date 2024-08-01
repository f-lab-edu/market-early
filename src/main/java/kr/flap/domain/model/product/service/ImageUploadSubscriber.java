package kr.flap.domain.model.product.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploadSubscriber implements MessageListener {
  private final RedisTemplate<String, Object> redisTemplate;
  private final ChannelTopic topic;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String encodedFile = (String) redisTemplate.getValueSerializer().deserialize(message.getBody());
    byte[] fileBytes = Base64.getDecoder().decode(encodedFile);
    MultipartFile file = new MockMultipartFile("file", fileBytes);
    log.info("Received message on topic: {}", new String(message.getChannel()));
    log.info("File received: {}", file.getOriginalFilename());
  }


  // 구독자 설정을 초기화
  @PostConstruct
  public void init() {
    redisTemplate.getConnectionFactory().getConnection().subscribe(this, topic.getTopic().getBytes());
  }
}
