package kr.flap.domain.model.product.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import kr.flap.domain.model.product.dto.ImageUploadResponse;
import kr.flap.domain.model.product.dto.UnsplashDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnsplashService {
  @Value("${UNSPLASH_API_KEY}")
  private String unSplashAccessKey;

  @Value("${UNSPLASH_API_URL}")
  private String apiUrl;

  @Value("${NAVER_CLOUD_ACCESS_KEY}")
  private String objectStorageAccessKey;

  @Value("${NAVER_CLOUD_SECRET_KEY}")
  private String objectStorageSecretKey;

  @Value("${NAVER_CLOUD_ENDPOINT}")
  private String endPoint;

  @Value("${NAVER_CLOUD_REGION}")
  private String regionName;

  private final RestTemplate restTemplate;

  public String getRandomImageJson() {
    String url = apiUrl + "?client_id=" + unSplashAccessKey;
    return restTemplate.getForObject(url, String.class);
  }

  public ByteArrayInputStream getRandomImageStream() throws IOException {
    String url = apiUrl + "?client_id=" + unSplashAccessKey;

    UnsplashDto response = restTemplate.getForObject(url, UnsplashDto.class);

    if (response != null && response.getLinks() != null) {
      String downloadLocation = response.getLinks().getDownloadLocation();
      log.info("downloadLocation = " + downloadLocation);

      byte[] imageData = downloadImage(response.getLinks().getDownload());
      if (imageData != null) {
        return new ByteArrayInputStream(imageData);
      }
    }
    return new ByteArrayInputStream(new byte[0]);
  }


// 다른 import 문들과 함께 추가

  public ImageUploadResponse uploadImage() throws IOException {
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(objectStorageAccessKey, objectStorageSecretKey)))
            .build();

    String bucketName = "unsplash";
    String objectKey = "test/" + UUID.randomUUID().toString() + ".jpg"; // 고유한 파일 이름 생성

    // 다운로드한 이미지 데이터를 바이트 배열로 읽음
    ByteArrayInputStream imageStream = getRandomImageStream();
    byte[] imageData = imageStream.readAllBytes(); // Convert stream to byte array to get the size
    imageStream.close(); // 스트림 닫기

    // 이미지 스트림을 다시 생성
    ByteArrayInputStream uploadStream = new ByteArrayInputStream(imageData);

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(imageData.length); // Set the actual size of the image data
    objectMetadata.setContentType("image/jpeg"); // Set the MIME type of the image

    log.info("File Size = " + imageData.length);

    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, uploadStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead); // 객체를 공개로 설정

    try {
      System.out.format("Uploading file to %s/%s\n", bucketName, objectKey);
      PutObjectResult result = s3.putObject(putObjectRequest);
      // 업로드된 객체의 URL 생성
      String objectUrl = s3.getUrl(bucketName, objectKey).toString();
      System.out.println("Object URL: " + objectUrl);

      String eTag = result.getETag();

      System.out.println("eTag = " + eTag);
      // 데이터베이스에 저장
      return new ImageUploadResponse(objectUrl, eTag);

    } catch (AmazonS3Exception e) {
      e.printStackTrace();
    } catch (SdkClientException e) {
      e.printStackTrace();
    } finally {
      uploadStream.close(); // 업로드 스트림 닫기
    }

    return null;
  }


  private byte[] downloadImage(String downloadUrl) throws IOException {
    URL url = new URL(downloadUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    try (InputStream in = connection.getInputStream()) {
      return in.readAllBytes();
    }
  }
}
