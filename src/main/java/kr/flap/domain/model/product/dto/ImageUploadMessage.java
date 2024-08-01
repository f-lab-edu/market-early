package kr.flap.domain.model.product.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ImageUploadMessage {
  private String productId;
  private String encodedFile;

  public ImageUploadMessage(String productId, String encodedFile) {
    this.productId = productId;
    this.encodedFile = encodedFile;
  }
}
