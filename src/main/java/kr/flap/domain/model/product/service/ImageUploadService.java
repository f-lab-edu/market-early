package kr.flap.domain.model.product.service;

import kr.flap.domain.model.product.dto.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploadService {

  private final NaverCloudService naverCloudService;

  @Async
  public CompletableFuture<ImageUploadResponse> uploadImageAsync(MultipartFile file) {
    return CompletableFuture.supplyAsync(() -> {
      try (InputStream inputStream = file.getInputStream()) {
        return naverCloudService.uploadImage(inputStream, file.getOriginalFilename(), file.getSize(), file.getContentType());
      } catch (IOException e) {
        log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
        throw new RuntimeException("Failed to upload image", e);
      }
    });
  }
}
