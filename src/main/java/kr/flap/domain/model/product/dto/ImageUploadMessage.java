package kr.flap.domain.model.product.dto;

import lombok.Data;

@Data
public class ImageUploadMessage {
  private String productId;
  private String encodedFile;
  private long timestamp;

  public ImageUploadMessage(String productId, String encodedFile,long timestamp) {
    this.productId = productId;
    this.encodedFile = encodedFile;
    this.timestamp = timestamp;
  }
}
