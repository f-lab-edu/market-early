package kr.flap.domain.model.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnsplashDto {
  private String id;
  private String slug;
  private String createdAt;
  private String updatedAt;
  private String promotedAt;
  private int width;
  private int height;
  private String color;
  private String blurHash;
  private String description;
  private String altDescription;
  private Urls urls;
  private Links links;

  @Getter
  @Setter
  @NoArgsConstructor
  public class Urls {
    private String raw;
    private String full;
    private String regular;
    private String small;
    private String thumb;
    private String smallS3;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public class Links {
    private String self;
    private String html;
    private String download;
    @JsonProperty("download_location")
    private String downloadLocation;
  }
}