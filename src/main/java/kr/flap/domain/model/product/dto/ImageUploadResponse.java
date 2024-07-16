package kr.flap.domain.model.product.dto;

import lombok.Getter;

@Getter
public class ImageUploadResponse {
  private final String objectUrl;
  private final String eTag;

  public ImageUploadResponse(String objectUrl, String eTag) {
    this.objectUrl = objectUrl;
    this.eTag = eTag;
  }
}
