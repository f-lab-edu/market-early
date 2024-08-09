package kr.flap.domain.model.product.controller;

import kr.flap.domain.model.product.dto.ImageUploadMessage;
import kr.flap.domain.model.product.service.ImagePublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("test/products")
public class ProductTestController {

  private final ImagePublishService imagePublishService;

  /**
   * Redis Stream에 이미지 업로드 메시지를 발행하는 테스트 엔드포인트
   *
   * @param message 이미지 업로드 메시지
   * @return 처리 결과
   */
  @PostMapping("/redis/image")
  public ResponseEntity<String> publishTestImageUploadMessage(@RequestBody ImageUploadMessage message) {
    imagePublishService.publishTestImageUploadMessage(message);
    return ResponseEntity.ok("Successfully published message to Redis Stream.");
  }
}
