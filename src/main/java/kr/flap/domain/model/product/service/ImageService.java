package kr.flap.domain.model.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImageService {

  @Value("${UNSPLASH_API_KEY}")
  private String unsplashApiKey;

  @Value("${UNSPLASH_API_URL}")
  private String unsplashApiUrl;

  private final RestTemplate restTemplate;

  public ImageService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void downloadImage() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Client-ID " + unsplashApiKey);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
            unsplashApiUrl,
            HttpMethod.GET,
            entity,
            String.class
    );

    if (response.getBody() != null) {
      response.getBody();
    } else {
      throw new RuntimeException("Failed to download image JSON");
    }
  }
}
